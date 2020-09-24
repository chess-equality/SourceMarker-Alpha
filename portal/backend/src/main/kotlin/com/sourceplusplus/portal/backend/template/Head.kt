package com.sourceplusplus.portal.backend.template

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
        src = "frontend.js"
    }
}

fun HEAD.overviewHead(title: String, block: (HEAD.() -> Unit)? = null) {
    commonHead(title)
    script {
        src = "echarts.min.js"
    }
    block?.let { it() }
}

fun HEAD.tracesHead(title: String, block: (HEAD.() -> Unit)? = null) {
    commonHead(title)
    block?.let { it() }
}

fun HEAD.configurationHead(title: String, block: (HEAD.() -> Unit)? = null) {
    commonHead(title)
    block?.let { it() }
}
