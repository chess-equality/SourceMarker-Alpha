package frontend.display

import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.OverviewTabOpened
import extensions.eb
import frontend.display.views.OverviewView
import kotlinx.browser.localStorage


class OverviewDisplay {
    val view = OverviewView()
    val portalUuid = "null"

    init {
        console.log("Connecting portal");
        eb.onopen = {
            js("portalConnected()")
            js("clickedViewAverageResponseTimeChart();") //default = avg resp time

            eb.registerHandler("$portalUuid-ClearOverview") { error: String, message: Any ->
                js("clearOverview();")
            }

            eb.registerHandler("$portalUuid-DisplayCard") { error: String, message: Any ->
                js("displayCard(message.body);")
            }

            eb.registerHandler("$portalUuid-UpdateChart") { error: String, message: Any ->
                js("updateChart(message.body);")
            }

            var timeFrame = localStorage.getItem("spp.metric_time_frame");
            if (timeFrame == null) {
                timeFrame = view.currentTimeFrame.name
                localStorage.setItem("spp.metric_time_frame", timeFrame)
            }
            js("updateTime(timeFrame);")
            js("portalLog('Set initial time frame to: ' + timeFrame);")

            eb.publish(OverviewTabOpened, "{'portal_uuid': '$portalUuid'}")
        }
    }
}