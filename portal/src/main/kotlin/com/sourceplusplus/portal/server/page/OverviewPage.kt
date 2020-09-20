package com.sourceplusplus.portal.server.page

import com.sourceplusplus.portal.server.portal
import com.sourceplusplus.portal.server.template.*
import com.sourceplusplus.protocol.portal.PageType.*
import com.sourceplusplus.protocol.portal.ChartItemType.*
import com.sourceplusplus.protocol.portal.TimeIntervalType.*
import com.sourceplusplus.protocol.artifact.trace.TraceOrderType.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML

/**
 * todo: description
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class OverviewPage {
    fun renderPage(): String {
        return buildString {
            appendLine("<!DOCTYPE html>")
            appendHTML().portal {
                overviewPage("Overview - SourceMarker") {
                    portalNav {
                        navItem(OVERVIEW, isActive = true)
                        navItem(TRACES) {
                            navSubItem(LATEST_TRACES, SLOWEST_TRACES, FAILED_TRACES)
                        }
                        //navItem(CONFIGURATION)
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
                    overviewScripts()
                }
            }
        }
    }
}

fun HTML.overviewPage(title: String, block: FlowContent.() -> Unit) {
    head {
        overviewHead(title)
    }
    body("overflow_y_hidden") {
        block()
    }
}