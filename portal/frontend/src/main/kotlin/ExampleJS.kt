import frontend.display.OverviewDisplay
import frontend.display.page.ConfigurationPage
import frontend.display.page.TracesPage
import kotlinx.browser.window

fun main() {
    println("calling overview display")
    println("path: " + window.location.pathname)
    println("the path: " + window.location.pathname)

    jq().ready {
        when (window.location.pathname) {
            "/traces" -> TracesPage().renderPage()
            "/configuration" -> ConfigurationPage().renderPage()
            else -> OverviewDisplay()
        }

        js("loadTheme();")
    }

//    jq().ready {
//        var inc = 0
//        window.setInterval({
//            inc++
//
//            jq("#card_throughput_average_header").text("Updated from Kotlin (JQuery) - $inc")
//
//            document.getElementById("card_responsetime_average_header")
//                ?.textContent = "Updated from Kotlin (DOM) - $inc"
//
//            val slaCard = document.getElementById("card_servicelevelagreement_average_header")!!
//            slaCard.textContent = "Updated from Kotlin (DOM) - $inc"
//        }, 1000)
//    }
}