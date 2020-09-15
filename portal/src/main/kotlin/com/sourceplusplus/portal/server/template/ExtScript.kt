package com.sourceplusplus.portal.server.template

import kotlinx.html.FlowContent
import kotlinx.html.script

private fun FlowContent.commonExtScripts() {
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
}

fun FlowContent.overviewScripts() {
    commonExtScripts()
    script {
        src = "js/overview.js"
    }
    script {
        src = "js/views/overview_view.js"
    }
}

fun FlowContent.tracesScripts() {
    commonExtScripts()
    script {
        src = "js/traces.js"
    }
    script {
        src = "js/views/traces_view.js"
    }
    script {
        src = "themes/default/assets/all.min.js"
    }
}

fun FlowContent.configurationScripts() {
    commonExtScripts()
    script {
        src = "js/configuration.js"
    }
    script {
        src = "js/views/configuration_view.js"
    }
}