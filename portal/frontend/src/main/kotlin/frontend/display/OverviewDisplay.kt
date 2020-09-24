package frontend.display

import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.OverviewTabOpened
import com.sourceplusplus.protocol.ProtocolAddress.Portal.Companion.ClearOverview
import com.sourceplusplus.protocol.ProtocolAddress.Portal.Companion.DisplayCard
import com.sourceplusplus.protocol.ProtocolAddress.Portal.Companion.UpdateChart
import com.sourceplusplus.protocol.portal.MetricType
import com.sourceplusplus.protocol.portal.QueryTimeFrame
import extensions.eb
import frontend.display.page.OverviewPage
import jq
import kotlinx.browser.localStorage
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class OverviewDisplay {
    val portalUuid = "null"
    var currentMetricType: MetricType = MetricType.Throughput_Average
    var currentTimeFrame = QueryTimeFrame.LAST_5_MINUTES

    init {
        println("init overview display")
        OverviewPage().renderPage()

        console.log("Overview tab started")
        console.log("Connecting portal")
        eb.onopen = {
            js("portalConnected()")
            clickedViewAverageResponseTimeChart() //default = avg resp time

            eb.registerHandler(ClearOverview(portalUuid)) { error: String, message: Any ->
                js("clearOverview();")
            }
            eb.registerHandler(DisplayCard(portalUuid)) { error: String, message: Any ->
                js("displayCard(message.body);")
            }
            eb.registerHandler(UpdateChart(portalUuid)) { error: String, message: Any ->
                js("updateChart(message.body);")
            }

            var timeFrame = localStorage.getItem("spp.metric_time_frame")
            if (timeFrame == null) {
                timeFrame = currentTimeFrame.name
                localStorage.setItem("spp.metric_time_frame", timeFrame)
            }
            updateTime(QueryTimeFrame.valueOf(timeFrame.toUpperCase()))
            js("portalLog('Set initial time frame to: ' + timeFrame);")

            eb.publish(OverviewTabOpened, "{'portal_uuid': '$portalUuid'}")
        }
    }

    fun updateTime(interval: QueryTimeFrame) {
        console.log("Update time: $interval")
        currentTimeFrame = interval
        localStorage.setItem("spp.metric_time_frame", interval.name)
        eb.send(
            "SetMetricTimeFrame",
            JsonObject(
                mapOf(
                    "portal_uuid" to JsonPrimitive(portalUuid),
                    "metric_time_frame" to JsonPrimitive(interval.name)
                )
            )
        )

        jq("#last_5_minutes_time").removeClass("active")
        jq("#last_15_minutes_time").removeClass("active")
        jq("#last_30_minutes_time").removeClass("active")
        jq("#last_hour_time").removeClass("active")
        jq("#last_3_hours_time").removeClass("active")

        jq("#" + interval.name.toLowerCase() + "_time").addClass("active")
    }

    fun clickedViewAverageThroughputChart() {
        console.log("Clicked view average throughput")
        currentMetricType = MetricType.valueOf("Throughput_Average")
        eb.send(
            "SetActiveChartMetric",
            JsonObject(
                mapOf(
                    "portal_uuid" to JsonPrimitive(portalUuid),
                    "metric_type" to JsonPrimitive(currentMetricType.name)
                )
            )
        )
    }

    fun clickedViewAverageResponseTimeChart() {
        console.log("Clicked view average response time")
        currentMetricType = MetricType.valueOf("ResponseTime_Average")
        eb.send(
            "SetActiveChartMetric",
            JsonObject(
                mapOf(
                    "portal_uuid" to JsonPrimitive(portalUuid),
                    "metric_type" to JsonPrimitive(currentMetricType.name)
                )
            )
        )
    }

    fun clickedViewAverageSLAChart() {
        console.log("Clicked view average SLA")
        currentMetricType = MetricType.valueOf("ServiceLevelAgreement_Average")
        eb.send(
            "SetActiveChartMetric",
            JsonObject(
                mapOf(
                    "portal_uuid" to JsonPrimitive(portalUuid),
                    "metric_type" to JsonPrimitive(currentMetricType.name)
                )
            )
        )
    }
}