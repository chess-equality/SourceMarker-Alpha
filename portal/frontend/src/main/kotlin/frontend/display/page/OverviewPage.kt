package frontend.display.page

import com.sourceplusplus.protocol.artifact.trace.TraceOrderType.*
import com.sourceplusplus.protocol.portal.ChartItemType.*
import com.sourceplusplus.protocol.portal.PageType.*
import com.sourceplusplus.protocol.portal.TimeIntervalType.*
import frontend.display.template.*
import kotlinx.browser.document
import kotlinx.html.HTML
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.visitAndFinalize
import org.w3c.dom.Element

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class OverviewPage {
    fun renderPage() {
        println("rending overview")
        val root: Element = document.getElementById("body")!!
        root.innerHTML = ""
        root.append {
            portalNav {
                navItem(OVERVIEW, isActive = true)
                navItem(TRACES) {
                    navSubItem(LATEST_TRACES, SLOWEST_TRACES, FAILED_TRACES)
                }
                navItem(CONFIGURATION)
            }
            overviewContent {
                navBar {
                    timeDropdown(FIVE_MINUTES, FIFTEEN_MINUTES, THIRTY_MINUTES, ONE_HOUR, THREE_HOURS)
                    calendar()

                    rightAlign {
                        externalPortalButton()
                    }
                }
                areaChart {
                    chartItem(AVG_THROUGHPUT)
                    chartItem(AVG_RESPONSE_TIME, isActive = true)
                    chartItem(AVG_SLA)
                }
            }
        }

        js("loadChart();")
    }
}


fun <T, C : TagConsumer<T>> C.portal(namespace: String? = null, block: HTML.() -> Unit = {}):
        T = HTML(kotlinx.html.emptyMap, this, namespace).visitAndFinalize(this, block)
