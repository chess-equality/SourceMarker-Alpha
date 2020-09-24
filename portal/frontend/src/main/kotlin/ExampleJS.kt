import kotlinx.browser.document
import kotlinx.browser.window

fun main() {
    jq().ready {
        var inc = 0
        window.setInterval({
            inc++

            jq("#card_throughput_average_header").text("Updated from Kotlin (JQuery) - $inc")

            document.getElementById("card_responsetime_average_header")
                ?.textContent = "Updated from Kotlin (DOM) - $inc"

            val slaCard = document.getElementById("card_servicelevelagreement_average_header")!!
            slaCard.textContent = "Updated from Kotlin (DOM) - $inc"
        }, 1000)
    }
}