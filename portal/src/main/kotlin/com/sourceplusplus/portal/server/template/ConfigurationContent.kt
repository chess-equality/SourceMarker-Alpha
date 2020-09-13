package com.sourceplusplus.portal.server.template

import kotlinx.html.*

fun FlowContent.configurationContent(cssClasses: String = "pusher background_color", block: FlowContent.() -> Unit) {
    div(cssClasses) {
        block()
    }
}

fun FlowContent.configurationTable(cssClasses: String = "ui grid marginlefting min_height", block: FlowContent.() -> Unit) {
    div(cssClasses) {
        block()
    }
}

fun FlowContent.artifactConfiguration() {
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
                        label("secondary_background_color") { +"Entry method" }
                    }
                }
                div("field") {
                    div("ui toggle checkbox") {
                        id = "auto_subscribe_toggle"
                        input {
                            id = "auto_subscribe_checkbox"
                            type = InputType.checkBox
                        }
                        label("secondary_background_color") { +"Auto-subscribe" }
                    }
                }
            }
        }
    }
}

fun FlowContent.artifactInformation() {
    div("ten wide column background_color min_height") {
        div("ui segments secondary_background_color margin_right_white") {
            table("ui small very compact very basic collapsing celled table margin_left_no_padding") {
                tbody {
                    tr {
                        td {
                            h4("ui header secondary_background_color") {
                                div("content") { +"Qualified Name" }
                            }
                        }
                        td("secondary_background_color") {
                            id = "artifact_qualified_name"
                        }
                    }
                    tr {
                        td {
                            h4("ui header secondary_background_color") {
                                div("content") { +"Create Date" }
                            }
                        }
                        td("secondary_background_color") {
                            id = "artifact_create_date"
                        }
                    }
                    tr {
                        td {
                            h4("ui header secondary_background_color") {
                                div("content") { +"Last Updated" }
                            }
                        }
                        td("secondary_background_color") {
                            id = "artifact_last_updated"
                        }
                    }
                    tr {
                        td {
                            h4("ui header secondary_background_color") {
                                div("content") { +"Endpoint" }
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