package frontend.display.template

import kotlinx.html.*

fun FlowContent.sidebar(block: FlowContent.() -> Unit) {
    block()
}
