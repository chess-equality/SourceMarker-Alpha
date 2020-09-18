package com.sourceplusplus.monitor.skywalking.track

import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import com.sourceplusplus.monitor.skywalking.model.GetEndpointTraces
import com.sourceplusplus.protocol.artifact.trace.TraceResult
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

                val traces = skywalkingClient.queryBasicTraces(
                    request.endpointId,
                    duration = request.zonedDuration.toDuration(skywalkingClient)
                )

                val traceStack = emptyList<TraceResult>()
//                if (traces != null) {
//                    traces.traces.forEach {
//                        it.traceIds.forEach { traceId ->
//                            val traceResult = skywalkingClient.queryTraceStack(traceId = traceId)
//                            if (traceResult != null) {
//                                traceStack.add(traceResult.)
//                            }
//                        }
//                    }
//                }

                it.reply(traces)
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