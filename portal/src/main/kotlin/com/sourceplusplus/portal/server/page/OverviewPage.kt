package com.sourceplusplus.portal.server.page

import kotlinx.html.*
import kotlinx.html.stream.appendHTML

class OverviewPage {
    fun renderPage(): String {
        return buildString {
            appendLine("<!DOCTYPE html>")
            appendHTML().html {
                head {
                    meta {
                        charset = "UTF-8"
                    }
                    title { +"""Overview - Source++""" }
                    link {
                        rel = "stylesheet"
                        href = "semantic.min.css"
                    }
                    script {
                        src = "echarts.min.js"
                    }
                    script {
                        src = "jquery-3.5.1.min.js"
                    }
                    script {
                        src = "portal_theme.js"
                    }
                }
                body("overflow_y_hidden") {
                    div("ui sidebar vertical left menu overlay visible very thin icon spp_blue webkit_transition") {
                        style = "overflow: visible !important;"
                        div("ui accordion displaynone") {
                            a(classes = "item openbtn openbtn_background_white") {
                                +"""Close menu"""
                            }
                            a(classes = "item active_tab") { +"""Dashboard""" }
                            div("title item inactive_tab") {
                                i("dropdown icon") {
                                }
                                +"""Traces"""
                            }
                            div("content") {
                                a(classes = "item sidebar_sub_text_color") {
                                    id = "sidebar_traces_link_latest"
                                    href = "traces"
                                    +"Latest"
                                }
                                a(classes = "item sidebar_sub_text_color") {
                                    id = "sidebar_traces_link_slowest"
                                    href = "traces"
                                    +"Slowest"
                                }
                                a(classes = "item sidebar_sub_text_color") {
                                    id = "sidebar_traces_link_failed"
                                    href = "traces"
                                    +"Failed"
                                }
                            }
                            a(classes = "item inactive_tab") {
                                id = "sidebar_configuration_link"
                                href = "configuration"
                                +"""Configuration"""
                            }
                        }
                        div("ui dropdown item openbtn") {
                            i("icon demo-icon content white_color") {
                            }
                        }
                        a(classes = "ui dropdown item active_tab") {
                            i("icon demo-icon dashboard") {
                            }
                        }
                        div("ui dropdown item inactive_tab") {
                            unsafe {
                                +"""<z class="displaynone">Traces</z>"""
                            }
                            i("icon demo-icon code") {
                            }
                            div("menu secondary_background_color") {
                                a(classes = "item") {
                                    id = "traces_link_latest"
                                    href = "traces"
                                    span("menu_tooltip_text") { +"Latest" }
                                }
                                a(classes = "item") {
                                    id = "traces_link_slowest"
                                    href = "traces"
                                    span("menu_tooltip_text") { +"Slowest" }
                                }
                                a(classes = "item") {
                                    id = "traces_link_failed"
                                    href = "traces"
                                    span("menu_tooltip_text") { +"Failed" }
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
                    div("pusher") {
                        div("ui menu fixed asd marginlefting middle aligned content menu_style") {
                            div("ui sidebar horizontal top menu fixed overlay visible very thin icon background_color webkit_transition") {
                                style = "overflow: visible !important;"
                                div("marginlefting align_content_center") {
                                    div("ui icon top left pointing dropdown") {
                                        i("clock outline icon spp_red_color spp_font_margin") {
                                        }
                                        div("menu secondary_background_color no_top_margin") {
                                            div("item") {
                                                id = "last_5_minutes_time"
                                                onClick = "updateTime('last_5_minutes')"
                                                span("menu_tooltip_text") { +"""LAST 5 MINUTES""" }
                                            }
                                            div("item") {
                                                id = "last_15_minutes_time"
                                                onClick = "updateTime('last_15_minutes')"
                                                span("menu_tooltip_active_text") { +"""LAST 15 MINUTES""" }
                                            }
                                            div("item") {
                                                id = "last_30_minutes_time"
                                                onClick = "updateTime('last_30_minutes')"
                                                span("menu_tooltip_text") { +"""LAST 30 MINUTES""" }
                                            }
                                            div("item") {
                                                id = "last_hour_time"
                                                onClick = "updateTime('last_hour')"
                                                span("menu_tooltip_text") { +"""LAST HOUR""" }
                                            }
                                            div("item") {
                                                id = "last_3_hours_time"
                                                onClick = "updateTime('last_3_hours')"
                                                span("menu_tooltip_text") { +"""LAST 3 HOURS""" }
                                            }
                                        }
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
                        }
                        div("ui padded equal height grid background_color") {
                            style = "min-height: 100vh; margin-left: 60px !important"
                            div("twelve wide stretched column") {
                                div("ui equal height grid") {
                                    div("one column row padding_top_bottom") {
                                        div("column padding_left_right") {
                                            div("full_height") {
                                                id = "overview_chart"
                                            }
                                        }
                                    }
                                }
                            }
                            div("four wide stretched column middle aligned") {
                                div("ui divided link items items_font") {
                                    div("item") {
                                        div("ui mini statistic") {
                                            onClick = "clickedViewAverageThroughputChart()"
                                            div("value align_left") {
                                                id = "card_throughput_average_header"
                                                +"""n/a"""
                                            }
                                            div("label") {
                                                id = "card_throughput_average_header_label"
                                                +"""AVG THROUGHPUT"""
                                            }
                                        }
                                    }
                                    div("item") {
                                        div("ui mini statistic") {
                                            onClick = "clickedViewAverageResponseTimeChart()"
                                            div("value spp_red_color align_left") {
                                                id = "card_responsetime_average_header"
                                                +"""n/a"""
                                            }
                                            div("label spp_red_color") {
                                                id = "card_responsetime_average_header_label"
                                                +"""AVG RESP TIME"""
                                            }
                                        }
                                    }
                                    div("item") {
                                        div("ui mini statistic") {
                                            onClick = "clickedViewAverageSLAChart()"
                                            div("value align_left") {
                                                id = "card_servicelevelagreement_average_header"
                                                +"""n/a"""
                                            }
                                            div("label") {
                                                id = "card_servicelevelagreement_average_header_label"
                                                +"""AVG SLA"""
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
                        src = "js/overview.js"
                    }
                    script {
                        src = "js/views/overview_view.js"
                    }
                }
            }
        }
    }
}