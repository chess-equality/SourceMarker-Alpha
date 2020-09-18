package com.sourceplusplus.monitor.skywalking.track

import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import com.sourceplusplus.monitor.skywalking.model.GetEndpointTraces
import io.vertx.core.Vertx
import io.vertx.kotlin.core.eventbus.requestAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.launch
import monitor.skywalking.protocol.trace.QueryBasicTracesQuery

class EndpointTracesTracker(private val skywalkingClient: SkywalkingClient) : CoroutineVerticle() {

    override suspend fun start() {
        vertx.eventBus().localConsumer<GetEndpointTraces>("$address.getTraces") {
            launch(vertx.dispatcher()) {
                val request = it.body()
                it.reply(skywalkingClient.getEndpointTraces(request.endpointId))
            }
        }
    }

    companion object {
        private const val address = "monitor.skywalking.endpoint.traces"

        suspend fun getTraces(request: GetEndpointTraces, vertx: Vertx): QueryBasicTracesQuery.Result {
            return vertx.eventBus()
                .requestAwait<QueryBasicTracesQuery.Result>("$address.getTraces", request)
                .body()
        }
    }
}