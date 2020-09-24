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

            eb.registerHandler("$portalUuid-ClearOverview") { error: String, message: String ->
                println("Error: $error")
                println("Message: $message")
                js("clearOverview();") as Unit
            }

            eb.registerHandler("$portalUuid-DisplayCard") { error: String, message: String ->
                js("displayCard(message.body);") as Unit
            }

            eb.registerHandler("$portalUuid-UpdateChart") { error: String, message: String ->
                js("updateChart(message.body);") as Unit
            }

            var timeFrame = localStorage.getItem("spp.metric_time_frame");
            if (timeFrame == null) {
                timeFrame = view.currentTimeFrame.name
                localStorage.setItem("spp.metric_time_frame", timeFrame)
            }
            js("updateTime(timeFrame);")
            //portalLog('Set initial time frame to: ' + timeFrame);

            eb.publish(OverviewTabOpened, "{'portal_uuid': '$portalUuid'}")
        }
    }
}