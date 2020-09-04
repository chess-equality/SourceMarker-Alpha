package com.sourceplusplus.portal.server.page

import kotlinx.html.*
import kotlinx.html.stream.appendHTML

class TracesPage {
    fun renderPage(): String {
        return buildString {
            appendLine("<!DOCTYPE html>")
            appendHTML().html {
                head {
                    meta {
                        charset = "UTF-8"
                    }
                    title { + """Traces - Source++"""}
                    link {
                        rel = "stylesheet"
                        href = "semantic.min.css"
                    }
                    script {
                        src = "jquery-3.5.1.min.js"
                    }
                    script {
                        src = "portal_theme.js"
                    }
                }
                body {
                    div("ui sidebar vertical left menu overlay visible very thin icon spp_blue webkit_transition") {
                        style = "overflow: visible !important;"
                        div("ui accordion displaynone") {
                            a(classes = "item openbtn openbtn_background_white") {
                                + """Close menu"""
                            }
                            a(classes = "item inactive_tab") {
                                id = "sidebar_overview_link"
                                href = "overview"
                                + """Dashboard"""
                            }
                            div("title item active_tab") {
                                i("dropdown icon") {
                                }
                                + """Traces"""
                            }
                            div("content") {
                                a(classes = "item sidebar_sub_text_color") {
                                    id = "sidebar_traces_link_latest"
                                    href = "traces"
                                    + "Latest"
                                }
                                a(classes = "item sidebar_sub_text_color") {
                                    id = "sidebar_traces_link_slowest"
                                    href = "traces"
                                    + "Slowest"
                                }
                                a(classes = "item sidebar_sub_text_color") {
                                    id = "sidebar_traces_link_failed"
                                    href = "traces"
                                    + "Failed"
                                }
                            }
                            a(classes = "item inactive_tab") {
                                id = "sidebar_configuration_link"
                                href = "configuration"
                                + """Configuration"""
                            }
                        }
                        div("ui dropdown item openbtn") {
                            i("icon demo-icon content white_color") {
                            }
                        }
                        a(classes = "ui item hide_on_toggle") {
                            id = "overview_link"
                            href = "overview"
                            i("icon demo-icon dashboard inactive_tab") {
                            }
                        }
                        div("ui dropdown item active_tab") {
                            unsafe {
                                +"""<z class="displaynone">Traces</z>"""
                            }
                            i("icon demo-icon code") {
                            }
                            div("menu secondary_background_color") {
                                a(classes = "item") {
                                    id = "traces_link_latest"
                                    href = "traces"
                                    span("menu_tooltip_text") { + "Latest"}
                                }
                                a(classes = "item") {
                                    id = "traces_link_slowest"
                                    href = "traces"
                                    span("menu_tooltip_text") { + "Slowest"}
                                }
                                div("dropdown-divider") {
                                }
                                a(classes = "item") {
                                    id = "traces_link_failed"
                                    href = "traces"
                                    span("menu_tooltip_text") { + "Failed"}
                                }
                            }
                        }
                        a(classes = "ui item hide_on_toggle") {
                            id = "configuration_link"
                            href = "configuration"
                            i("icon configure inactive_tab") {
                            }
                        }
                    }
                    div("pusher background_color") {
                        div("ui menu borderless marginlefting background_color menu_style_2") {
                            a(classes = "ui item dropdown active_sub_tab") {
                                id = "latest_traces_header"
                                onClick = "clickedBackToTraces()"
                                span {
                                    id = "latest_traces_header_text"
                                    + """Latest Traces"""
                                }
                                div("menu") {
                                    id = "latest_traces_menu"
                                    div("ui input item") {
                                        i("play icon spp_blue_color") {
                                        }
                                        input {
                                            classes = setOf("input_width")
                                            id = "traces_start_field"
                                            type = InputType.text
                                            readonly = true
                                        }
                                    }
                                    div("ui input item") {
                                        i("stop icon spp_red_color") {
                                        }
                                        input {
                                            classes = setOf("input_width")
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
                                            + """Total: 0"""
                                        }
                                    }
                                }
                            }
                            a(classes = "ui item dropdown visibility_hidden") {
                                id = "trace_stack_header"
                                onClick = "clickedBackToTraceStack()"
                                span {
                                    id = "trace_stack_header_text"
                                    + """Trace Stack"""
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
                                    + """Span Info"""
                                }
                            }
                            div("right menu align_content_center") {
                                a {
                                    id = "current_page_external"
                                    href = "#"
                                    onClick = "clickedViewAsExternalPortal()"
                                    i("external link icon spp_red_color spp_font_margin") {
                                    }
                                }
                            }
                        }
                        div("wide column marginlefting") {
                            table("ui celled striped table unstackable secondary_background_color no_top_margin") {
                                id = "top_trace_table"
                                thead {
                                    tr {
                                        th(classes = "secondary_background_color trace_th") {
                                            + """Operation"""
                                        }
                                        th(classes = "secondary_background_color trace_th_center") {
                                            + """Occurred"""
                                        }
                                        th(classes = "secondary_background_color trace_th_center") {
                                            + """Exec"""
                                        }
                                        th(classes = "secondary_background_color trace_th_center") {
                                            + """Status"""
                                        }
                                    }
                                }
                                tbody {
                                    id = "trace_table"
                                }
                            }
                            table("ui celled striped table unstackable trace_stack_table hidden_full_height") {
                                id = "trace_stack_table"
                                thead("secondary_background_color") {
                                    tr {
                                        th(classes = "secondary_background_color trace_th") {
                                            + """Operation"""
                                        }
                                        th(classes = "secondary_background_color trace_th_center") {
                                            + """Exec"""
                                        }
                                        th(classes = "secondary_background_color trace_th_center") {
                                            + """Exec (%)"""
                                        }
                                        th(classes = "secondary_background_color trace_th_center") {
                                            + """Status"""
                                        }
                                    }
                                }
                                tbody("stack_table_background") {
                                    id = "stack_table"
                                }
                            }
                            div("visibility_hidden") {
                                id = "span_info_panel"
                                div("ui segments") {
                                    div("ui segment span_segment_background") {
                                        p {
                                            + """Start time:"""
                                            span {
                                                id = "span_info_start_time"
                                            }
                                            + """("""
                                            span("trace_time") {
                                                id = "span_info_start_trace_time"
                                            }
                                            + """)"""
                                        }
                                        p {
                                            + """Start time:"""
                                            span {
                                                id = "span_info_end_time"
                                            }
                                            + """("""
                                            span("trace_time") {
                                                id = "span_info_end_trace_time"
                                            }
                                            + """)"""
                                        }
                                    }
                                    div("ui segment displaynone no_padding") {
                                        id = "span_tag_div"
                                        table("ui celled striped table unstackable") {
                                            id = "span_tag_table"
                                            thead {
                                                tr {
                                                    th { + """Tag"""}
                                                    th { + """Value"""}
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
                                                    th { + """Trace Logs"""}
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
                    }
                    script {
                        src = "semantic.min.js"
                    }
                    script {
                        src = "themes/default/assets/all.min.js"
                    }
                    script {
                        src = "moment.min.js"
                    }
                    script {
                        src = "sockjs.min.js"
                    }
                    script {
                        src = "vertx-eventbus.min.js"
                    }
                    script {
                        src = "source_eventbus_bridge.js"
                    }
                    script {
                        src = "js/traces.js"
                    }
                    script {
                        src = "js/views/traces_view.js"
                    }
                }
            }
        }
    }
}