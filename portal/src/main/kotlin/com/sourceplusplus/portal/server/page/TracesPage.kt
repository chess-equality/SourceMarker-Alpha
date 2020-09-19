package com.sourceplusplus.portal.server.page

import com.sourceplusplus.portal.server.portal
import com.sourceplusplus.portal.server.template.*
import com.sourceplusplus.protocol.portal.PageType.*
import com.sourceplusplus.protocol.artifact.trace.TraceOrderType.*
import com.sourceplusplus.protocol.artifact.trace.TraceSpanInfoType.*
import com.sourceplusplus.protocol.artifact.trace.TraceStackHeaderType.*
import com.sourceplusplus.protocol.artifact.trace.TraceTableType.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML

/**
 * todo: description
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class TracesPage {
    fun renderPage(): String {
        return buildString {
            appendLine("<!DOCTYPE html>")
            appendHTML().portal {
                tracesPage("Traces - SourceMarker") {
                    portalNav {
                        navItem(OVERVIEW)
                        navItem(TRACES, isActive = true) {
                            navSubItem(LATEST_TRACES, SLOWEST_TRACES, FAILED_TRACES)
                        }
                        //navItem(CONFIGURATION)
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