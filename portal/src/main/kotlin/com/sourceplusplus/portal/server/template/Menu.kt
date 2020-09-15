package com.sourceplusplus.portal.server.template

import com.sourceplusplus.portal.server.model.*
import kotlinx.html.*

fun FlowContent.menu(cssClasses: String = "ui accordion displaynone",
                     block: FlowContent.() -> Unit) {
    div(cssClasses) {
        a(classes = "item openbtn openbtn_background_white") {
            +"Close menu"
        }
        block()
    }
    div("ui dropdown item openbtn") {
        i("icon demo-icon content white_color") {
        }
    }
}

fun FlowContent.menuItem(pageType: PageType, isActive: Boolean = false, block: FlowContent.(activeClass: String) -> Unit) {
    pageType.applyThis(this, true, isActive, block)
}

fun FlowContent.subMenuItem(vararg traceTypes: TraceType = arrayOf()) {
    for (traceType in traceTypes) {
        a(classes = "item sidebar_sub_text_color") {
            id = "sidebar_traces_link_${traceType.id}"
            href = traceType.id
            +traceType.description
        }
    }
}