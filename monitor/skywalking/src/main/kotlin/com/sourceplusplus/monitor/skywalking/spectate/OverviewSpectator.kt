package com.sourceplusplus.monitor.skywalking.spectate

import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import com.sourceplusplus.monitor.skywalking.model.GetEndpointMetrics
import com.sourceplusplus.monitor.skywalking.model.ZonedDuration
import com.sourceplusplus.monitor.skywalking.toDoubleArray
import com.sourceplusplus.monitor.skywalking.track.EndpointMetricsTracker
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.OverviewTabOpened
import com.sourceplusplus.protocol.ProtocolAddress.Portal.Companion.DisplayCard
import com.sourceplusplus.protocol.ProtocolAddress.Portal.Companion.UpdateChart
import com.sourceplusplus.protocol.portal.*
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.ZonedDateTime

class OverviewSpectator : CoroutineVerticle() {

    private val decimalFormat = DecimalFormat(".#")

    override suspend fun start() {
        vertx.eventBus().localConsumer<JsonObject>(OverviewTabOpened) {
            println("here")
            //todo: get sourcemark or get portal uuid & endpointId
        }
    }

    private fun updateOverview(endpointId: String) {
        GlobalScope.launch(vertx.dispatcher()) {
            val metricsRequest = GetEndpointMetrics(
                listOf("endpoint_cpm", "endpoint_avg", "endpoint_sla", "endpoint_percentile"),
                endpointId,
                ZonedDuration(
                    ZonedDateTime.now().minusMinutes(15),
                    ZonedDateTime.now(),
                    SkywalkingClient.DurationStep.MINUTE
                )
            )
            val metrics = EndpointMetricsTracker.getMetrics(metricsRequest, vertx)

            val seriesData =
                SplineSeriesData(
                    0,
                    metricsRequest.toInstantTimes().map { it.toEpochMilliseconds() }, //todo: toInstantTimes() only
                    metrics[0].toDoubleArray()
                )
            val splineChart =
                SplineChart(
                    MetricType.Throughput_Average,
                    QueryTimeFrame.LAST_15_MINUTES,
                    listOf(seriesData)
                )
            vertx.eventBus().publish(UpdateChart("null"), JsonObject(Json.encode(splineChart)))

            val throughputAverageCard =
                BarTrendCard(
                    meta = "throughput_average",
                    header = toPrettyFrequency(calculateAverage(metrics[0].toDoubleArray()) / 60.0)
                )
            vertx.eventBus().publish(DisplayCard("null"), JsonObject(Json.encode(throughputAverageCard)))

            val responseTimeAverageCard =
                BarTrendCard(
                    meta = "responsetime_average",
                    header = toPrettyDuration(calculateAverage(metrics[1].toDoubleArray()).toInt())
                )
            vertx.eventBus().publish(DisplayCard("null"), JsonObject(Json.encode(responseTimeAverageCard)))

            val slaAvg = calculateAverage(metrics[2].toDoubleArray())
            val slaAverageCard =
                BarTrendCard(
                    meta = "servicelevelagreement_average",
                    header = if (slaAvg == 0.0) {
                        "0%"
                    } else {
                        decimalFormat.format(slaAvg / 100.0) + "%"
                    }
                )
            vertx.eventBus().publish(DisplayCard("null"), JsonObject(Json.encode(slaAverageCard)))
        }
    }

    private fun calculateAverage(values: DoubleArray): Double {
//        val histogram = Histogram(UniformReservoir(values.size))
//        values.forEach {
//            histogram.update(it.toInt())
//        }
//        return histogram.snapshot.mean
        return 0.0
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
}