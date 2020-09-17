package com.sourceplusplus.portal.server.template

import com.sourceplusplus.portal.server.model.ChartItemType
import kotlinx.html.*

fun FlowContent.overviewContent(block: FlowContent.() -> Unit) {
    div("pusher") {
        block()
    }
}

fun FlowContent.areaChart(block: (FlowContent.() -> Unit)? = null) {
    div("ui padded equal height grid background_color") {
        style = "min-height: 100vh; margin-left: 60px !important"
        div("twelve wide stretched column") {
            div("ui equal height grid") {
                div("one column row padding_top_bottom") {
                    div("column padding_left_right") {
                        div("full_height") {
                            id = "overview_chart"
                        }
                    }
                }
            }
        }
        div("four wide stretched column middle aligned") {
            div("ui divided link items items_font") {
                block?.let { it() }
            }
        }
    }
}

fun FlowContent.chartItem(chartItemType: ChartItemType, isActive: Boolean = false) {
    val isActiveClass = if (isActive) "spp_red_color" else ""
    div("item") {
        div("ui mini statistic") {
            onClick = "clickedView${chartItemType.type.capitalize()+chartItemType.description}Chart()"
            div("value align_left $isActiveClass".trim()) {
                id = "card_${chartItemType.id}_${chartItemType.type}_header"
                +"n/a"
            }
            div("label align_left $isActiveClass".trim()) {
                id = "card_${chartItemType.id}_${chartItemType.type}_header_label"
                +"${chartItemType.abbr} ${chartItemType.label}"
            }
        }
    }
}