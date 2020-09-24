package extensions

import kotlinx.serialization.json.JsonObject

val vertx: Vertx = Vertx()
val eb: Vertx.EventBus = vertx.eventBus.init()

@Suppress("unused")
class Vertx {
    private val _eventBus: EventBus = EventBus()

    val eventBus: EventBus
        get() = _eventBus

    class EventBus {
        companion object {
            @JsName("callKotlinOnOpen")
            val callKotlinOnOpen by lazy { return@lazy eb.onopen::invoke }
        }

        @JsName("kotlinEventBusOnOpen")
        var onopen: () -> Unit = { }

        fun init(): EventBus {
            js("eb.onopen = function () { frontend.extensions.Vertx.EventBus.Companion.callKotlinOnOpen(); }")
            return this
        }

        @Deprecated("Use jsonObject")
        fun send(address: String, json: String) {
            js("eb.send(address, (0, eval)('(' + json + ')'));")
        }

        fun send(address: String, jsonObject: JsonObject) {
            send(address, jsonObject.toString().replace("\"", "'"))
        }

        fun publish(address: String, json: String) {
            js("eb.publish(address, (0, eval)('(' + json + ')'));")
        }

        fun registerHandler(address: String, handler: (error: String, message: Any) -> Unit) {
            js("eb.registerHandler(address, handler);")
        }
    }
}
