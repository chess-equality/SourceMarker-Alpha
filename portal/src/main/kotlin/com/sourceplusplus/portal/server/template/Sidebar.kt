package com.sourceplusplus.portal.server.template

import kotlinx.html.*

fun FlowContent.sidebar(block: FlowContent.() -> Unit) {
    block()
}
