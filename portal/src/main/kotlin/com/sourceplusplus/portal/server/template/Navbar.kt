package com.sourceplusplus.portal.server.template

import com.sourceplusplus.portal.server.model.TimeIntervalType
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

fun FlowContent.timeDropdown(vararg timeIntervalTypes: TimeIntervalType = arrayOf(), cssClasses: String = "marginlefting align_content_center") {
    div(cssClasses) {
        div("ui icon top left pointing dropdown") {
            i("clock outline icon spp_red_color spp_font_margin") {
            }
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

fun FlowContent.tracesHeader(cssClasses: String = "") {
    a(classes = "ui item dropdown active_sub_tab") {
        id = "latest_traces_header"
        onClick = "clickedBackToTraces()"
        span {
            id = "latest_traces_header_text"
            + "Latest Traces"
        }
        div("menu") {
            id = "latest_traces_menu"
            div("ui input item") {
                i("play icon spp_blue_color") {
                }
                input {
                    classes = setOf(cssClasses, "input_width")
                    id = "traces_start_field"
                    type = InputType.text
                    readonly = true
                }
            }
            div("ui input item") {
                i("stop icon spp_red_color") {
                }
                input {
                    classes = setOf(cssClasses, "input_width")
                    id = "traces_stop_field"
                    type = InputType.text
                    readonly = true
                }
            }
            div("divider") {
            }
            div("ui input item align_center") {
                label {
                    id = "traces_total_label"
                    + "Total: 0"
                }
            }
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
            div("ui input item") {
                i("crosshairs icon no_padding_top") {
                }
                input {
                    classes = setOf("input_width")
                    id = "trace_id_field"
                    type = InputType.text
                    readonly = true
                }
            }
            div("ui input item") {
                i("clock outline icon no_padding_top") {
                }
                input {
                    classes = setOf("input_width")
                    id = "time_occurred_field"
                    type = InputType.text
                    readonly = true
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
        i("external link icon spp_red_color spp_font_margin") {
        }
    }
}

fun FlowContent.leftAlign(block: FlowContent.() -> Unit) {
    block()
}

fun FlowContent.rightAlign(cssClasses: String = "menu align_content_center", block: FlowContent.() -> Unit) {
    val css = "right $cssClasses".trim()
    div(css) {
        block()
    }
}