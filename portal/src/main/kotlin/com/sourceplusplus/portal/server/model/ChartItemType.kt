package com.sourceplusplus.portal.server.model

import kotlinx.html.*

enum class ChartItemType {
    AVG_THROUGHPUT {
        override fun applyThis(flowContent: FlowContent, cssClasses: String) {
            flowContent.apply {
                div(cssClasses.ifEmpty { cssClass }) {
                    onClick = "clickedViewAverageThroughputChart()"
                    div("value align_left") {
                        id = "card_throughput_average_header"
                        +"n/a"
                    }
                    div("label align_left") {
                        id = "card_throughput_average_header_label"
                        +"AVG THROUGHPUT"
                    }
                }
            }
        }
    },
    AVG_RESPONSE_TIME {
        override fun applyThis(flowContent: FlowContent, cssClasses: String) {
            flowContent.apply {
                div(cssClasses.ifEmpty { cssClass }) {
                    onClick = "clickedViewAverageResponseTimeChart()"
                    div("value spp_red_color align_left") {
                        id = "card_responsetime_average_header"
                        +"n/a"
                    }
                    div("label align_left spp_red_color") {
                        id = "card_responsetime_average_header_label"
                        +"AVG RESP TIME"
                    }
                }
            }
        }
    },
    AVG_SLA {
        override fun applyThis(flowContent: FlowContent, cssClasses: String) {
            flowContent.apply {
                div(cssClasses.ifEmpty { cssClass }) {
                    onClick = "clickedViewAverageSLAChart()"
                    div("value align_left") {
                        id = "card_servicelevelagreement_average_header"
                        +"n/a"
                    }
                    div("label align_left") {
                        id = "card_servicelevelagreement_average_header_label"
                        +"AVG SLA"
                    }
                }
            }
        }
    };

    val cssClass = "ui mini statistic"
    abstract fun applyThis(flowContent: FlowContent, cssClasses: String = "")
}