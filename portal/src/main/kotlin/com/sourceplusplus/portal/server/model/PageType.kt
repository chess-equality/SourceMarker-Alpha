package com.sourceplusplus.portal.server.model

import kotlinx.html.*

enum class PageType {
    OVERVIEW {
        override fun applyThis(flowContent: FlowContent, isMenuItem: Boolean, isActive: Boolean, block: FlowContent.(activeClass: String) -> Unit) {
            flowContent.apply {
                if (isMenuItem) {
                    if (isActive) {
                        a(classes = "item active_tab") { +"Dashboard" }
                    } else {
                        a(classes = "item inactive_tab") {
                            id = "sidebar_overview_link"
                            href = "overview"
                            +"Dashboard"
                        }
                    }
                } else {
                    if (isActive) {
                        a(classes = "ui dropdown item active_tab") {
                            i("icon demo-icon dashboard") {
                            }
                        }
                    } else {
                        a(classes = "ui item hide_on_toggle") {
                            id = "overview_link"
                            href = "overview"
                            i("icon demo-icon dashboard inactive_tab") {
                            }
                        }
                    }
                }
            }
        }
    },
    TRACES {
        override fun applyThis(flowContent: FlowContent, isMenuItem: Boolean, isActive: Boolean, block: FlowContent.(activeClass: String) -> Unit) {
            flowContent.apply {
                if (isMenuItem) {
                    var activeClass = "active_tab"
                    if (!isActive) {
                        activeClass = "inactive_tab"
                    }
                    div("title item") {
                        if (activeClass.isNotEmpty()) {
                            classes = classes.plus(activeClass)
                        }
                        i("dropdown icon") {
                        }
                        +"Traces"
                    }
                    div("content") {
                        block(activeClass)
                    }
                } else {
                    var activeClass = "active_tab"
                    if (!isActive) {
                        activeClass = "inactive_tab"
                    }
                    block(activeClass)
                }
            }
        }
    },
    CONFIGURATION {
        override fun applyThis(flowContent: FlowContent, isMenuItem: Boolean, isActive: Boolean, block: FlowContent.(activeClass: String) -> Unit) {
            flowContent.apply {
                if (isMenuItem) {
                    if (isActive) {
                        a(classes = "item active_tab") { + "Configuration" }
                    } else {
                        a(classes = "item inactive_tab") {
                            id = "sidebar_configuration_link"
                            href = "configuration"
                            +"Configuration"
                        }
                    }
                } else {
                    if (isActive) {
                        a(classes = "ui dropdown item active_tab") {
                            i("icon configure") {
                            }
                        }
                    } else {
                        a(classes = "ui item hide_on_toggle") {
                            id = "configuration_link"
                            href = "configuration"
                            i("icon configure inactive_tab") {
                            }
                        }
                    }
                }
            }
        }
    };

    abstract fun applyThis(flowContent: FlowContent, isMenuItem: Boolean, isActive: Boolean = false, block: FlowContent.(activeClass: String) -> Unit)
}