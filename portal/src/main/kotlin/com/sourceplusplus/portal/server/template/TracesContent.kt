package com.sourceplusplus.portal.server.template

import kotlinx.html.*

fun FlowContent.tracesContent(cssClasses: String = "pusher background_color", block: FlowContent.() -> Unit) {
    div(cssClasses) {
        block()
    }
}

fun FlowContent.tracesTable(cssClasses: String = "wide column marginlefting", block: FlowContent.() -> Unit) {
    div(cssClasses) {
        block()
    }
}

fun FlowContent.topTraceTable() {
    table("ui celled striped table unstackable secondary_background_color no_top_margin") {
        id = "top_trace_table"
        thead {
            tr {
                th(classes = "secondary_background_color trace_th") {
                    + "Operation"
                }
                th(classes = "secondary_background_color trace_th_center") {
                    + "Occurred"
                }
                th(classes = "secondary_background_color trace_th_center") {
                    + "Exec"
                }
                th(classes = "secondary_background_color trace_th_center") {
                    + "Status"
                }
            }
        }
        tbody {
            id = "trace_table"
        }
    }
}

fun FlowContent.traceStackTable() {
    table("ui celled striped table unstackable trace_stack_table hidden_full_height") {
        id = "trace_stack_table"
        thead("secondary_background_color") {
            tr {
                th(classes = "secondary_background_color trace_th") {
                    + "Operation"
                }
                th(classes = "secondary_background_color trace_th_center") {
                    + "Exec"
                }
                th(classes = "secondary_background_color trace_th_center") {
                    + "Exec (%)"
                }
                th(classes = "secondary_background_color trace_th_center") {
                    + "Status"
                }
            }
        }
        tbody("stack_table_background") {
            id = "stack_table"
        }
    }
}

fun FlowContent.spanInfoPanel() {
    div("visibility_hidden") {
        id = "span_info_panel"
        div("ui segments") {
            div("ui segment span_segment_background") {
                p {
                    + "Start time:"
                    span {
                        id = "span_info_start_time"
                    }
                    + "("
                    span("trace_time") {
                        id = "span_info_start_trace_time"
                    }
                    + ")"
                }
                p {
                    + "Start time:"
                    span {
                        id = "span_info_end_time"
                    }
                    + "("
                    span("trace_time") {
                        id = "span_info_end_trace_time"
                    }
                    + ")"
                }
            }
            div("ui segment displaynone no_padding") {
                id = "span_tag_div"
                table("ui celled striped table unstackable") {
                    id = "span_tag_table"
                    thead {
                        tr {
                            th { + "Tag"}
                            th { + "Value"}
                        }
                    }
                    tbody {
                        id = "tag_table"
                    }
                }
            }
            div("ui segment displaynone no_padding_auto") {
                id = "span_log_div"
                table("ui celled striped table unstackable") {
                    id = "log_tag_table"
                    thead {
                        tr {
                            th { + "Trace Logs"}
                        }
                    }
                    tbody {
                        id = "log_table"
                    }
                }
            }
        }
    }
}