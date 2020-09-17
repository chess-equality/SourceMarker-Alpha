package com.sourceplusplus.portal.server.page

import com.sourceplusplus.portal.server.portal
import com.sourceplusplus.portal.server.template.*
import com.sourceplusplus.portal.server.model.PageType.*
import com.sourceplusplus.portal.server.model.trace.TraceType.*
import com.sourceplusplus.portal.server.model.trace.TraceTableType.*
import com.sourceplusplus.portal.server.model.trace.TraceSpanInfoType.*
import com.sourceplusplus.portal.server.model.trace.TraceStackHeaderType.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML

class TracesPage {
    fun renderPage(): String {
        return buildString {
            appendLine("<!DOCTYPE html>")
            appendHTML().portal {
                tracesPage("Traces - Source++") {
                    portalNav {
                        navItem(OVERVIEW)
                        navItem(TRACES, isActive = true) {
                            navSubItem(LATEST, SLOWEST, FAILED)
                        }
                        navItem(CONFIGURATION)
                    }
                    tracesContent {
                        tracesNavBar {
                            tracesHeader(TRACE_ID, TIME_OCCURRED)
                            rightAlign {
                                externalPortalButton()
                            }
                        }
                        tracesTable {
                            topTraceTable(OPERATION, OCCURRED, EXEC, STATUS)
                            traceStackTable(OPERATION, EXEC, EXEC_PCT, STATUS)
                            spanInfoPanel(START_TIME, END_TIME)
                        }
                    }
                    tracesScripts()
                }
            }
        }
    }
}

fun HTML.tracesPage(title: String, block: FlowContent.() -> Unit) {
    head {
        tracesHead(title)
    }
    body {
        block()
    }
}