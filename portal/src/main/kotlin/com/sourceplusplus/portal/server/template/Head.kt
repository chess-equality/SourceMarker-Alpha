package com.sourceplusplus.portal.server.template

import kotlinx.html.*

private fun HEAD.commonHead(title: String) {
    meta {
        charset = "UTF-8"
    }
    title { +title }
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

fun HEAD.overviewHead(title: String, block: HEAD.() -> Unit) {
    commonHead(title)
    script {
        src = "echarts.min.js"
    }
    block()
}

fun HEAD.tracesHead(title: String, block: HEAD.() -> Unit) {
    commonHead(title)
    block()
}

fun HEAD.configurationHead(title: String, block: HEAD.() -> Unit) {
    commonHead(title)
    block()
}