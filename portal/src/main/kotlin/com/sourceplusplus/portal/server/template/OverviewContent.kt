package com.sourceplusplus.portal.server.template

import com.sourceplusplus.portal.server.model.ChartItemType
import kotlinx.html.*

fun FlowContent.overviewContent(cssClasses: String = "pusher", block: FlowContent.() -> Unit) {
    div(cssClasses) {
        block()
    }
}

fun FlowContent.areaChart(vararg chartItemTypes: ChartItemType = arrayOf(), cssClasses: String = "ui padded equal height grid background_color") {
    div(cssClasses) {
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
                for (chartItemType in chartItemTypes) {
                    div("item") {
                        chartItemType.applyThis(this)
                    }
                }
            }
        }
    }
}