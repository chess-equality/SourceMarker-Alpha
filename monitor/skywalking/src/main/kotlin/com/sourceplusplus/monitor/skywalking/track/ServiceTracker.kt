package com.sourceplusplus.monitor.skywalking.track

import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import io.vertx.core.Vertx
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import monitor.skywalking.protocol.metadata.GetAllServicesQuery
import java.time.LocalDateTime

class ServiceTracker(private val skywalkingClient: SkywalkingClient) : CoroutineVerticle() {

    private val address = "monitor.skywalking.service"
    var currentService: GetAllServicesQuery.Result? = null
    var activeServices: List<GetAllServicesQuery.Result> = emptyList()

    override suspend fun start() {
        val consumer = vertx.eventBus().localConsumer<String>(address)
        consumer.handler { message ->
            // The consumer will get a failure
            message.fail(0, "it failed!!!")
        }

        GlobalScope.launch(vertx.dispatcher()) {
            activeServices = skywalkingClient.run {
                getServices(getDuration(LocalDateTime.now().minusMinutes(15)))
            }
            if (activeServices.size == 1) {
                currentService = activeServices[0]
            }
        }
    }

    companion object {
        fun currentServiceConsumer(vertx: Vertx): MessageConsumer<String> {
            return vertx.eventBus().localConsumer("todo")
        }

        fun activeServicesConsumer(vertx: Vertx): MessageConsumer<String> {
            return vertx.eventBus().localConsumer("todo")
        }

        suspend fun getCurrentService(vertx: Vertx): GetAllServicesQuery.Result? {
            TODO("this")
        }

        suspend fun getActiveServices(vertx: Vertx): GetAllServicesQuery.Result? {
            TODO("this")
        }
    }
}