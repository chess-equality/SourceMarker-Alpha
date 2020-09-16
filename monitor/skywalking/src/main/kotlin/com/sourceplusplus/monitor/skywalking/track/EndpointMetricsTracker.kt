package com.sourceplusplus.monitor.skywalking.track

import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import io.vertx.core.Vertx
import io.vertx.kotlin.core.eventbus.requestAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.launch

class EndpointMetricsTracker(private val skywalkingClient: SkywalkingClient) : CoroutineVerticle() {

    override suspend fun start() {
        vertx.eventBus().localConsumer<String>("$address.searchExactEndpoint") {
            launch(vertx.dispatcher()) {
                val service = ServiceTracker.getCurrentService(vertx)
                if (service != null) {
                    val endpoints = skywalkingClient.searchEndpoint(it.body(), service.id, 1)
                    if (endpoints.isNotEmpty()) {
                        it.reply(endpoints[0])
                    } else {
                        it.reply(null)
                    }
                } else {
                    it.reply(null)
                }
            }
        }
    }

    companion object {
        private const val address = "monitor.skywalking.endpoint.metrics"

//        suspend fun getMetrics(endpointId: String, vertx: Vertx): SearchEndpointQuery.Result? {
//            return vertx.eventBus()
//                .requestAwait<SearchEndpointQuery.Result?>("$address.searchExactEndpoint", keyword)
//                .body()
//        }
    }
}