package frontend.display.views

import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.SetActiveChartMetric
import com.sourceplusplus.protocol.portal.MetricType
import com.sourceplusplus.protocol.portal.QueryTimeFrame
import extensions.vertx

class OverviewView {

    private val portalUuid = "null"
    private var currentMetricType: MetricType = MetricType.Throughput_Average
    private var currentTimeFrame = QueryTimeFrame.LAST_5_MINUTES

    fun clickedViewAverageThroughputChart() {
        console.log("Clicked view average throughput")
        currentMetricType = MetricType.Throughput_Average
        vertx.eventBus.send(
            SetActiveChartMetric,
            "{'portal_uuid': '$portalUuid', 'metric_type': '$currentMetricType'}"
        )
    }
}