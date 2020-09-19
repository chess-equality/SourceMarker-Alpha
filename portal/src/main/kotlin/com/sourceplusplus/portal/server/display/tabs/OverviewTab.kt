package com.sourceplusplus.portal.server.display.tabs

import com.codahale.metrics.Histogram
import com.codahale.metrics.UniformReservoir
import com.sourceplusplus.portal.server.display.SourcePortal
import com.sourceplusplus.protocol.ArtifactNameUtils.getShortQualifiedFunctionName
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.ArtifactMetricUpdated
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.OverviewTabOpened
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.RefreshOverview
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.SetActiveChartMetric
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.SetMetricTimeFrame
import com.sourceplusplus.protocol.artifact.ArtifactMetricResult
import com.sourceplusplus.protocol.artifact.ArtifactMetrics
import com.sourceplusplus.protocol.portal.*
import com.sourceplusplus.protocol.portal.MetricType.*
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import org.slf4j.LoggerFactory
import java.text.DecimalFormat
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

/**
 * Displays general source code artifact statistics.
 * Useful for gathering an overall view of an artifact's runtime behavior.
 *
 * Viewable artifact metrics:
 *  - Average throughput
 *  - Average response time
 *  - 99/95/90/75/50 response time percentiles
 *  - Minimum/Maximum response time
 *  - Average SLA
 *
 * @since 0.0.1
 * @author <a href="mailto:bfergerson@apache.org">Brandon Fergerson</a>
 */
class OverviewTab : AbstractTab(PageType.OVERVIEW) {

    companion object {
        private val log = LoggerFactory.getLogger(OverviewTab::class.java)
    }

    val decimalFormat = DecimalFormat(".#")

    override fun start() {
        super.start()

        vertx.setPeriodic(5000) {
            SourcePortal.getPortals().forEach {
                if (it.currentTab == PageType.OVERVIEW) {
                    //todo: only update if external or internal and currently displaying
                    vertx.eventBus().send(RefreshOverview, it)
                }
            }
        }

        //refresh with stats from cache (if avail)
        vertx.eventBus().consumer<JsonObject>(OverviewTabOpened) {
            log.info("Overview tab opened")
            val portalUuid = it.body().getString("portal_uuid")
            val portal = SourcePortal.getPortal(portalUuid)!!
            portal.currentTab = thisTab
            SourcePortal.ensurePortalActive(portal)
            updateUI(portal)
        }
        vertx.eventBus().consumer<ArtifactMetricResult>(ArtifactMetricUpdated) {
            val artifactMetricResult = it.body()
            SourcePortal.getPortals(artifactMetricResult.appUuid!!, artifactMetricResult.artifactQualifiedName)
                .forEach { portal ->
                    portal.overviewView.cacheMetricResult(artifactMetricResult)
                    updateUI(portal)
                }
        }

        vertx.eventBus().consumer<JsonObject>(SetMetricTimeFrame) {
            val request = JsonObject.mapFrom(it.body())
            val portal = SourcePortal.getPortal(request.getString("portal_uuid"))!!
            val view = portal.overviewView
            view.timeFrame = QueryTimeFrame.valueOf(request.getString("metric_time_frame").toUpperCase())
            log.info("Overview time frame set to: " + view.timeFrame)
            updateUI(portal)

            vertx.eventBus().send(RefreshOverview, portal)
//            //subscribe (re-subscribe) to get latest stats
//            val subscribeRequest = ArtifactMetricSubscribeRequest.builder()
//                .appUuid(portal.appUuid)
//                .artifactQualifiedName(portal.portalUI.viewingPortalArtifact)
//                .timeFrame(view.timeFrame)
//                .metricTypes([Throughput_Average, ResponseTime_Average, ServiceLevelAgreement_Average]).build()
//            SourcePortalConfig.current.getCoreClient(portal.appUuid).subscribeToArtifact(subscribeRequest, {
//                if (it.succeeded()) {
//                    log.info("Successfully subscribed to metrics with request: " + subscribeRequest)
//                } else {
//                    log.error("Failed to subscribe to artifact metrics", it.cause())
//                }
//            })
        }
        vertx.eventBus().consumer<JsonObject>(SetActiveChartMetric) {
            val request = JsonObject.mapFrom(it.body())
            val portal = SourcePortal.getPortal(request.getString("portal_uuid"))!!
            portal.overviewView.activeChartMetric = valueOf(request.getString("metric_type"))
            updateUI(portal)

            vertx.eventBus().send(RefreshOverview, portal)
        }
        log.info("{} started", javaClass.simpleName)
    }

    override fun updateUI(portal: SourcePortal) {
        if (portal.currentTab != thisTab) {
            return
        }

        val artifactMetricResult = portal.overviewView.metricResult ?: return
        if (log.isTraceEnabled) {
            log.trace(
                "Artifact metrics updated. Portal uuid: {} - App uuid: {} - Artifact qualified name: {} - Time frame: {}",
                portal.portalUuid, artifactMetricResult.appUuid,
                getShortQualifiedFunctionName(artifactMetricResult.artifactQualifiedName),
                artifactMetricResult.timeFrame
            )
        }

        artifactMetricResult.artifactMetrics.forEach {
            updateCard(portal, artifactMetricResult, it)
            if (it.metricType == portal.overviewView.activeChartMetric) {
                updateSplineGraph(portal, artifactMetricResult, it)
            }
        }
    }

    fun updateSplineGraph(portal: SourcePortal, metricResult: ArtifactMetricResult, artifactMetrics: ArtifactMetrics) {
        val times = ArrayList<Instant>() //todo: no toJavaInstant/fromEpochMilliseconds
        var current = metricResult.start
        times.add(current)
        while (current.toJavaInstant().isBefore(metricResult.stop.toJavaInstant())) {
            if (metricResult.step == "MINUTE") {
                current =
                    Instant.fromEpochMilliseconds(current.toJavaInstant().plus(1, ChronoUnit.MINUTES).toEpochMilli())
                times.add(current)
            } else {
                throw UnsupportedOperationException("Invalid step: " + metricResult.step)
            }
        }

        val finalArtifactMetrics = if (artifactMetrics.metricType == ServiceLevelAgreement_Average) {
            artifactMetrics.copy(values = artifactMetrics.values.map { it / 100 })
        } else {
            artifactMetrics
        }

        val seriesIndex =
            when (finalArtifactMetrics.metricType) {
                ResponseTime_99Percentile -> 0
                ResponseTime_95Percentile -> 1
                ResponseTime_90Percentile -> 2
                ResponseTime_75Percentile -> 3
                ResponseTime_50Percentile -> 4
                else -> 0
            }
        val seriesData = SplineSeriesData(
            seriesIndex = seriesIndex,
            times = times.map { it.toEpochMilliseconds() }, //todo: no toEpochMilliseconds
            values = finalArtifactMetrics.values.map { it.toDouble() }.toDoubleArray() //todo: or this
        )
        val splintChart = SplineChart(
            metricType = finalArtifactMetrics.metricType,
            timeFrame = metricResult.timeFrame,
            seriesData = Collections.singletonList(seriesData)
        )
        val portalUuid = portal.portalUuid
        vertx.eventBus().publish("$portalUuid-UpdateChart", JsonObject(Json.encode(splintChart)))
    }

    fun updateCard(portal: SourcePortal, metricResult: ArtifactMetricResult, artifactMetrics: ArtifactMetrics) {
        val avg = calculateAverage(artifactMetrics)
        val percents = calculatePercents(artifactMetrics)

        when (artifactMetrics.metricType) {
            Throughput_Average -> {
                val barTrendCard = BarTrendCard(
                    timeFrame = metricResult.timeFrame,
                    header = toPrettyFrequency(avg / 60.0),
                    meta = artifactMetrics.metricType.toString().toLowerCase(),
                    barGraphData = percents
                )
                val portalUuid = portal.portalUuid
                vertx.eventBus().publish("$portalUuid-DisplayCard", JsonObject(Json.encode(barTrendCard)))
            }
            ResponseTime_Average -> {
                val barTrendCard = BarTrendCard(
                    timeFrame = metricResult.timeFrame,
                    header = toPrettyDuration(avg.toInt()),
                    meta = artifactMetrics.metricType.toString().toLowerCase(),
                    barGraphData = percents
                )
                val portalUuid = portal.portalUuid
                vertx.eventBus().publish("$portalUuid-DisplayCard", JsonObject(Json.encode(barTrendCard)))
            }
            ServiceLevelAgreement_Average -> {
                val barTrendCard = BarTrendCard(
                    timeFrame = metricResult.timeFrame,
                    header = if (avg == 0.0) {
                        "0%"
                    } else {
                        decimalFormat.format(avg / 100.0).toString() + "%"
                    },
                    meta = artifactMetrics.metricType.toString().toLowerCase(),
                    barGraphData = percents
                )
                val portalUuid = portal.portalUuid
                vertx.eventBus().publish("$portalUuid-DisplayCard", JsonObject(Json.encode(barTrendCard)))
            }
        }
    }

    private fun calculateAverage(artifactMetrics: ArtifactMetrics): Double {
        val histogram = Histogram(UniformReservoir(artifactMetrics.values.size))
        artifactMetrics.values.forEach {
            histogram.update(it)
        }
        return histogram.snapshot.mean
    }

    fun calculatePercents(artifactMetrics: ArtifactMetrics): DoubleArray {
        val metricArr = ArrayList<Int>()
        when (artifactMetrics.values.size) {
            60 -> {
                for (i in artifactMetrics.values.indices) {
                    metricArr.add(
                        artifactMetrics.values[i] + artifactMetrics.values[i + 1]
                                + artifactMetrics.values[i + 2] + artifactMetrics.values[i + 3]
                    )
                }
            }
            30 -> {
                for (i in artifactMetrics.values.indices step 2) {
                    metricArr.add(artifactMetrics.values[i] + artifactMetrics.values[i + 1])
                }
            }
            else -> {
                metricArr.addAll(artifactMetrics.values)
            }
        }

        val percentMax = metricArr.maxOrNull()!!
        val percents = ArrayList<Double>()
        for (i in metricArr.indices) {
            if (percentMax == 0) {
                percents.add(0.0)
            } else {
                percents.add((metricArr[i] / percentMax) * 100.00)
            }
        }
        return percents.toDoubleArray()
    }

    private fun toPrettyDuration(millis: Int): String {
        val days = millis / 86400000.0
        if (days > 1) {
            return "${days.toInt()}dys"
        }
        val hours = millis / 3600000.0
        if (hours > 1) {
            return "${hours.toInt()}hrs"
        }
        val minutes = millis / 60000.0
        if (minutes > 1) {
            return "${minutes.toInt()}mins"
        }
        val seconds = millis / 1000.0
        if (seconds > 1) {
            return "${seconds.toInt()}secs"
        }
        return "${millis}ms"
    }

    private fun toPrettyFrequency(perSecond: Double): String {
        return when {
            perSecond > 1000000.0 -> "${perSecond / 1000000.0.toInt()}M/sec"
            perSecond > 1000.0 -> "${perSecond / 1000.0.toInt()}K/sec"
            perSecond > 1.0 -> "${perSecond.toInt()}/sec"
            else -> "${(perSecond * 60.0).toInt()}/min"
        }
    }

//    private fun updateOverview(endpointId: String) {
//        GlobalScope.launch(vertx.dispatcher()) {
//            val metricsRequest = GetEndpointMetrics(
//                listOf("endpoint_cpm", "endpoint_avg", "endpoint_sla", "endpoint_percentile"),
//                endpointId,
//                ZonedDuration(
//                    ZonedDateTime.now().minusMinutes(15),
//                    ZonedDateTime.now(),
//                    SkywalkingClient.DurationStep.MINUTE
//                ) toInt
//            )
//            val metrics = EndpointMetricsTracker.getMetrics(metricsRequest, vertx)
//
//            val seriesData =
//                SplineSeriesData(
//                    0,
//                    metricsRequest.toInstantTimes().map { it.toEpochMilliseconds() }, //todo: toInstantTimes() only
//                    metrics[0].toDoubleArray()
//                )
//            val splineChart =
//                SplineChart(
//                    Throughput_Average,
//                    QueryTimeFrame.LAST_15_MINUTES,
//                    listOf(seriesData)
//                )
//            vertx.eventBus().publish(ProtocolAddress.Portal.UpdateChart("null"), JsonObject(Json.encode(splineChart)))
//
//            val throughputAverageCard =
//                BarTrendCard(
//                    meta = "throughput_average",
//                    header = toPrettyFrequency(calculateAverage(metrics[0].toDoubleArray()) / 60.0)
//                )
//            vertx.eventBus()
//                .publish(ProtocolAddress.Portal.DisplayCard("null"), JsonObject(Json.encode(throughputAverageCard)))
//
//            val responseTimeAverageCard =
//                BarTrendCard(
//                    meta = "responsetime_average",
//                    header = toPrettyDuration(calculateAverage(metrics[1].toDoubleArray()).toInt())
//                )
//            vertx.eventBus()
//                .publish(ProtocolAddress.Portal.DisplayCard("null"), JsonObject(Json.encode(responseTimeAverageCard)))
//
//            val slaAvg = calculateAverage(metrics[2].toDoubleArray())
//            val slaAverageCard =
//                BarTrendCard(
//                    meta = "servicelevelagreement_average",
//                    header = if (slaAvg == 0.0) {
//                        "0%"
//                    } else {
//                        decimalFormat.format(slaAvg / 100.0) + "%"
//                    }
//                )
//            vertx.eventBus()
//                .publish(ProtocolAddress.Portal.DisplayCard("null"), JsonObject(Json.encode(slaAverageCard)))
//        }
//    }
}
