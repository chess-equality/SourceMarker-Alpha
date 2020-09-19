package com.sourceplusplus.monitor.skywalking.track

import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import com.sourceplusplus.monitor.skywalking.SkywalkingClient.DurationStep
import io.vertx.core.Vertx
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.launch
import monitor.skywalking.protocol.metadata.GetServiceInstancesQuery
import java.time.ZonedDateTime

/**
 * todo: description
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class ServiceInstanceTracker(private val skywalkingClient: SkywalkingClient) : CoroutineVerticle() {

    var currentServiceInstance: GetServiceInstancesQuery.Result? = null
    var activeServicesInstances: List<GetServiceInstancesQuery.Result> = emptyList()

    override suspend fun start() {
        ServiceTracker.currentServiceConsumer(vertx).handler {
            //todo: whenever the current service changes, update active/current service instances
            launch(vertx.dispatcher()) {
                activeServicesInstances = skywalkingClient.run {
                    getServiceInstances(
                        it.body().id,
                        getDuration(ZonedDateTime.now().minusMinutes(15), DurationStep.MINUTE)
                    )
                }
                vertx.eventBus().publish("$address.activeServiceInstances-Updated", activeServicesInstances)

                if (activeServicesInstances.isNotEmpty()) {
                    currentServiceInstance = activeServicesInstances[0]
                    vertx.eventBus().publish("$address.currentServiceInstance-Updated", currentServiceInstance)
                }
            }
        }
    }

    companion object {
        private const val address = "monitor.skywalking.service.instance"

        fun currentServiceInstanceConsumer(vertx: Vertx): MessageConsumer<GetServiceInstancesQuery.Result> {
            return vertx.eventBus().localConsumer("$address.currentServiceInstance-Updated")
        }

        fun activeServiceInstancesConsumer(vertx: Vertx): MessageConsumer<List<GetServiceInstancesQuery.Result>> {
            return vertx.eventBus().localConsumer("$address.activeServiceInstances-Updated")
        }
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