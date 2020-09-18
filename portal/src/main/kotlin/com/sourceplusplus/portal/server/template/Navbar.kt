package com.sourceplusplus.portal.server.template

import com.sourceplusplus.protocol.portal.TimeIntervalType
import com.sourceplusplus.protocol.artifact.trace.TraceStackHeaderType
import kotlinx.html.*

fun FlowContent.overviewNavBar(block: FlowContent.() -> Unit) {
    div("ui menu fixed asd marginlefting middle aligned content menu_style") {
        div("ui sidebar horizontal top menu fixed overlay visible very thin icon background_color webkit_transition") {
            style = "overflow: visible !important;"
            block()
        }
    }
}

fun FlowContent.tracesNavBar(block: FlowContent.() -> Unit) {
    div("ui menu borderless marginlefting background_color menu_style_2") {
        block()
    }
}

fun FlowContent.configurationNavBar(block: FlowContent.() -> Unit) {
    div("ui menu marginlefting aligned content menu_style_3") {
        div("ui sidebar horizontal top menu fixed overlay visible very thin icon background_color overflow_visible") {
            block()
        }
    }
}

fun FlowContent.timeDropdown(vararg timeIntervalTypes: TimeIntervalType = arrayOf()) {
    div("marginlefting align_content_center") {
        div("ui icon top left pointing dropdown") {
            i("clock outline icon spp_red_color spp_font_margin")
            div("menu secondary_background_color no_top_margin") {
                for (timeIntervalType in timeIntervalTypes) {
                    div("item") {
                        id = "last_${timeIntervalType.id}_time"
                        onClick = "updateTime('last_${timeIntervalType.id}')"
                        span("menu_tooltip_text") { +"LAST ${timeIntervalType.description}" }
                    }
                }
            }
        }
    }
}

fun FlowContent.tracesHeader(vararg traceStackHeaderTypes: TraceStackHeaderType = arrayOf()) {
    a(classes = "ui item dropdown active_sub_tab") {
        id = "latest_traces_header"
        onClick = "clickedBackToTraces()"
        span {
            id = "latest_traces_header_text"
            + "Latest Traces"
        }
    }
    a(classes = "ui item dropdown visibility_hidden") {
        id = "trace_stack_header"
        onClick = "clickedBackToTraceStack()"
        span {
            id = "trace_stack_header_text"
            + "Trace Stack"
        }
        div("menu") {
            id = "trace_stack_menu"
            for (traceStackHeaderType in traceStackHeaderTypes) {
                div("ui input item") {
                    i("icon no_padding_top ${traceStackHeaderType.icon}")
                    input {
                        classes = setOf("input_width")
                        id = "${traceStackHeaderType.id}_field"
                        type = InputType.text
                        readonly = true
                    }
                }
            }
        }
    }
    a(classes = "ui item dropdown visibility_hidden") {
        id = "span_info_header"
        span {
            id = "span_info_header_text"
            + "Span Info"
        }
    }
}

fun FlowContent.externalPortalButton() {
    a {
        id = "current_page_external"
        href = "#"
        onClick = "clickedViewAsExternalPortal()"
        i("external link icon spp_red_color spp_font_margin")
    }
}

fun FlowContent.rightAlign(block: FlowContent.() -> Unit) {
    div("right menu align_content_center") {
        block()
    }
}