package com.sourceplusplus.portal.backend.template

import kotlinx.html.*

fun FlowContent.sidebar(block: FlowContent.() -> Unit) {
    block()
}
