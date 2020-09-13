package com.sourceplusplus.portal.server.page

import com.sourceplusplus.portal.server.portal
import com.sourceplusplus.portal.server.template.*
import com.sourceplusplus.portal.server.model.PageType.*
import com.sourceplusplus.portal.server.model.TraceType.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML

class TracesPage {
    fun renderPage(): String {
        return buildString {
            appendLine("<!DOCTYPE html>")
            appendHTML().portal {
                tracesPage {
                    leftNav {
                        menu {
                            menuItem(OVERVIEW) {}
                            menuItem(TRACES, isActive = true) {
                                subMenuItem(LATEST, SLOWEST, FAILED)
                            }
                            menuItem(CONFIGURATION) {}
                        }
                        sidebar {
                            tabs {
                                tabItem(OVERVIEW) {}
                                tabItem(TRACES, isActive = true) { activeClass ->
                                    subTabItem(activeClass, LATEST, SLOWEST, FAILED)
                                }
                                tabItem(CONFIGURATION) {}
                            }
                        }
                    }
                    tracesContent {
                        tracesNavBar {
                            leftAlign {
                                tracesHeader()
                            }
                            rightAlign {
                                externalPortalButton()
                            }
                        }
                        tracesTable {
                            topTraceTable()
                            traceStackTable()
                            spanInfoPanel()
                        }
                    }
                    tracesScripts()
                }
            }
        }
    }
}

fun HTML.tracesPage(title: String = "Traces - Source++", bodyClass: String = "", block: FlowContent.() -> Unit) {
    head {
        tracesHead(title) {}
    }
    body(bodyClass) {
        block()
    }
}