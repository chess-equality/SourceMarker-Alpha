package frontend.display.views

import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.SetActiveChartMetric
import com.sourceplusplus.protocol.portal.MetricType
import com.sourceplusplus.protocol.portal.QueryTimeFrame
import extensions.eb

class OverviewView {

    private val portalUuid = "null"
    var currentMetricType: MetricType = MetricType.Throughput_Average
    var currentTimeFrame = QueryTimeFrame.LAST_5_MINUTES

    fun clickedViewAverageThroughputChart() {
        console.log("Clicked view average throughput")
        currentMetricType = MetricType.Throughput_Average
        eb.send(
            SetActiveChartMetric,
            "{'portal_uuid': '$portalUuid', 'metric_type': '$currentMetricType'}"
        )
    }
}