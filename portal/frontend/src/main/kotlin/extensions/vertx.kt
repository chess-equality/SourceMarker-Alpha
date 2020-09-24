package extensions

val vertx: Vertx = Vertx()

class Vertx {
    private val _eventBus: EventBus = EventBus()

    val eventBus: EventBus
        get() = _eventBus

    class EventBus {
        fun send(address: String, json: String) {
            js("eb.send(address, (0, eval)('(' + json + ')'));")
        }
    }
}