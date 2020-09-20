package com.sourceplusplus.portal.server.template

import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.style

fun FlowContent.leftNav(
    cssClasses: String = "ui sidebar vertical left menu overlay visible very thin icon spp_blue webkit_transition",
    block: DIV.() -> Unit
) {
    div(cssClasses) {
        style = "overflow: visible !important;"
        block()
    }
}
