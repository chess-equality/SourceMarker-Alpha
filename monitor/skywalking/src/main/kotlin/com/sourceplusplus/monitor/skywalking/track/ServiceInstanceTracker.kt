package com.sourceplusplus.monitor.skywalking.track

import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import io.vertx.kotlin.coroutines.CoroutineVerticle
import monitor.skywalking.protocol.metadata.GetServiceInstancesQuery

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
        }
    }
}