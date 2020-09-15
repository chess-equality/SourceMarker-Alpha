package com.sourceplusplus.portal.server.page

import com.sourceplusplus.portal.server.portal
import com.sourceplusplus.portal.server.template.*
import com.sourceplusplus.portal.server.model.PageType.*
import com.sourceplusplus.portal.server.model.TraceType.*
import com.sourceplusplus.portal.server.model.ChartItemType.*
import com.sourceplusplus.portal.server.model.TimeIntervalType.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML

class OverviewPage {
    fun renderPage(): String {
        return buildString {
            appendLine("<!DOCTYPE html>")
            appendHTML().portal {
                overviewPage {
                    leftNav {
                        menu {
                            menuItem(OVERVIEW, isActive = true) {}
                            menuItem(TRACES) {
                                subMenuItem(LATEST, SLOWEST, FAILED)
                            }
                            menuItem(CONFIGURATION) {}
                        }
                        sidebar {
                            tabs {
                                tabItem(OVERVIEW, isActive = true) {}
                                tabItem(TRACES) { activeClass ->
                                    subTabItem(activeClass, LATEST, SLOWEST, FAILED)
                                }
                                tabItem(CONFIGURATION) {}
                            }
                        }
                    }
                    overviewContent {
                        overviewNavBar {
                            leftAlign {
                                timeDropdown(FIVE_MINUTES, FIFTEEN_MINUTES, THIRTY_MINUTES, ONE_HOUR, THREE_HOURS)
                            }
                            rightAlign {
                                externalPortalButton()
                            }
                        }
                        areaChart(AVG_THROUGHPUT, AVG_RESPONSE_TIME, AVG_SLA)
                    }
                    overviewScripts()
                }
            }
        }
    }
}

fun HTML.overviewPage(title: String = "Overview - Source++", bodyClass: String = "overflow_y_hidden", block: FlowContent.() -> Unit) {
    head {
        overviewHead(title) {}
    }
    body(bodyClass) {
        block()
    }
}