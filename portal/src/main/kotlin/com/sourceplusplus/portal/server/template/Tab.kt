package com.sourceplusplus.portal.server.template

import com.sourceplusplus.portal.server.model.*
import kotlinx.html.*

fun FlowContent.tabs(block: FlowContent.() -> Unit) {
    block()
}

fun FlowContent.tabItem(pageType: PageType, isActive: Boolean = false, block: FlowContent.(activeClass: String) -> Unit) {
    pageType.applyThis(this, false, isActive, block)
}

fun FlowContent.subTabItem(activeClass: String = "", vararg traceTypes: TraceType = arrayOf()) {
    div("ui dropdown item") {
        if (activeClass.isNotEmpty()) {
            classes = classes.plus(activeClass)
        }
        unsafe {
            +"""<z class="displaynone">Traces</z>"""
        }
        i("icon demo-icon code") {
        }
        div("menu secondary_background_color") {
            for (traceType in traceTypes) {
                a(classes = "item") {
                    id = "traces_link_${traceType.id}"
                    href = traceType.id
                    span("menu_tooltip_text") { +traceType.description }
                }
            }
        }
    }
}