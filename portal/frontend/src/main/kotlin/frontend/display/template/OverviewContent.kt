package frontend.display.template

import com.sourceplusplus.protocol.portal.ChartItemType
import kotlinx.html.*
import org.w3c.dom.HTMLElement

fun TagConsumer<HTMLElement>.overviewContent(block: FlowContent.() -> Unit) {
    div("pusher") {
        block()
    }
}

fun TagConsumer<HTMLElement>.areaChart(block: (FlowContent.() -> Unit)? = null) {
    div("ui padded equal height grid background_color") {
        style = "min-height: calc(100vh - 47px); margin-left: 60px !important" //todo: has to be better way to do this
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
            onClick = "clickedView${chartItemType.type.capitalize() + chartItemType.description}Chart()"
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
