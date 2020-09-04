package com.sourceplusplus.portal.server.page

import kotlinx.html.*
import kotlinx.html.stream.appendHTML

class ConfigurationPage {
    fun renderPage(): String {
        return buildString {
            appendLine("<!DOCTYPE html>")
            appendHTML().html {
                head {
                    meta {
                        charset = "UTF-8"
                    }
                    title { +"""Configuration - Source++""" }
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
                                +"""Close menu"""
                            }
                            a(classes = "item inactive_tab") {
                                id = "sidebar_overview_link"
                                href = "overview"
                                +"""Dashboard"""
                            }
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
                            a(classes = "item active_tab") { +"""Configuration""" }
                        }
                        div("ui dropdown item openbtn") {
                            i("icon demo-icon content white_color") {
                            }
                        }
                        a(classes = "ui item hide_on_toggle") {
                            id = "overview_link"
                            href = "configuration"
                            i("icon dashboard inactive_tab") {
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
                        a(classes = "ui dropdown item active_tab") {
                            i("icon configure") {
                            }
                        }
                    }
                    div("pusher background_color") {
                        div("ui menu marginlefting aligned content menu_style_3") {
                            div("ui sidebar horizontal top menu fixed overlay visible very thin icon background_color overflow_visible") {
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
                        div("ui grid marginlefting min_height") {
                            div("six wide column min_height") {
                                div("ui form") {
                                    div("ui segment secondary_background_color") {
                                        div("field") {
                                            div("ui toggle checkbox") {
                                                id = "entry_method_toggle"
                                                input {
                                                    id = "entry_method_checkbox"
                                                    type = InputType.checkBox
                                                }
                                                label("secondary_background_color") { +"""Entry method""" }
                                            }
                                        }
                                        div("field") {
                                            div("ui toggle checkbox") {
                                                id = "auto_subscribe_toggle"
                                                input {
                                                    id = "auto_subscribe_checkbox"
                                                    type = InputType.checkBox
                                                }
                                                label("secondary_background_color") { +"""Auto-subscribe""" }
                                            }
                                        }
                                    }
                                }
                            }
                            div("ten wide column background_color min_height") {
                                div("ui segments secondary_background_color margin_right_white") {
                                    table("ui small very compact very basic collapsing celled table margin_left_no_padding") {
                                        tbody {
                                            tr {
                                                td {
                                                    h4("ui header secondary_background_color") {
                                                        div("content") { +"""Qualified Name""" }
                                                    }
                                                }
                                                td("secondary_background_color") {
                                                    id = "artifact_qualified_name"
                                                }
                                            }
                                            tr {
                                                td {
                                                    h4("ui header secondary_background_color") {
                                                        div("content") { +"""Create Date""" }
                                                    }
                                                }
                                                td("secondary_background_color") {
                                                    id = "artifact_create_date"
                                                }
                                            }
                                            tr {
                                                td {
                                                    h4("ui header secondary_background_color") {
                                                        div("content") { +"""Last Updated""" }
                                                    }
                                                }
                                                td("secondary_background_color") {
                                                    id = "artifact_last_updated"
                                                }
                                            }
                                            tr {
                                                td {
                                                    h4("ui header secondary_background_color") {
                                                        div("content") { +"""Endpoint""" }
                                                    }
                                                }
                                                td("secondary_background_color") {
                                                    id = "artifact_endpoint"
                                                }
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
                        src = "js/configuration.js"
                    }
                    script {
                        src = "js/views/configuration_view.js"
                    }
                }
            }
        }
    }
}