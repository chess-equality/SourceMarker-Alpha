package com.sourceplusplus.monitor.skywalking.track

import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import com.sourceplusplus.monitor.skywalking.SkywalkingClient.DurationStep
import io.vertx.core.Vertx
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import monitor.skywalking.protocol.metadata.GetAllServicesQuery
import monitor.skywalking.protocol.metadata.GetServiceInstancesQuery
import java.time.LocalDateTime

class ServiceInstanceTracker(private val skywalkingClient: SkywalkingClient) : CoroutineVerticle() {

    private val address = "monitor.skywalking.service.instance"
    var currentServiceInstance: GetServiceInstancesQuery.Result? = null
    var activeServicesInstances: List<GetServiceInstancesQuery.Result> = emptyList()

    override suspend fun start() {
        val consumer = vertx.eventBus().localConsumer<String>(address)
        consumer.handler { message ->
            // The consumer will get a failure
            message.fail(0, "it failed!!!")
        }

        ServiceTracker.currentServiceConsumer(vertx).handler {
            //todo: whenever the current service changes, update active/current service instances
            GlobalScope.launch(vertx.dispatcher()) {
                activeServicesInstances = skywalkingClient.run {
                    getServiceInstances(
                        it.body().id,
                        getDuration(LocalDateTime.now().minusMinutes(15), DurationStep.MINUTE)
                    )
                }
                if (activeServicesInstances.size == 1) {
                    currentServiceInstance = activeServicesInstances[0]

                    vertx.eventBus().publish("$address.currentServiceInstance-Updated", currentServiceInstance)
                }
            }
        }
    }

    companion object {
        fun currentServiceInstanceConsumer(vertx: Vertx): MessageConsumer<GetServiceInstancesQuery.Result> {
            return vertx.eventBus().localConsumer("monitor.skywalking.service.instance.currentServiceInstance-Updated")
        }

//        fun activeServicesConsumer(vertx: Vertx): MessageConsumer<String> {
//            return vertx.eventBus().localConsumer("todo")
//        }
//
//        suspend fun getCurrentService(vertx: Vertx): GetAllServicesQuery.Result? {
//            TODO("this")
//        }
//
//        suspend fun getActiveServices(vertx: Vertx): GetAllServicesQuery.Result? {
//            TODO("this")
//        }
    }
}