package com.sourceplusplus.monitor.skywalking.track

import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import com.sourceplusplus.monitor.skywalking.model.GetEndpointTraces
import com.sourceplusplus.monitor.skywalking.toProtocol
import com.sourceplusplus.protocol.artifact.trace.Trace
import com.sourceplusplus.protocol.artifact.trace.TraceOrderType
import com.sourceplusplus.protocol.artifact.trace.TraceResult
import com.sourceplusplus.protocol.artifact.trace.TraceSpanStackQueryResult
import io.vertx.core.Vertx
import io.vertx.kotlin.core.eventbus.requestAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.launch

/**
 * todo: description
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class EndpointTracesTracker(private val skywalkingClient: SkywalkingClient) : CoroutineVerticle() {

    override suspend fun start() {
        vertx.eventBus().localConsumer<GetEndpointTraces>("$address.getTraces") {
            launch(vertx.dispatcher()) {
                val request = it.body()

                val traces = skywalkingClient.queryBasicTraces(
                    request.endpointId,
                    duration = request.zonedDuration.toDuration(skywalkingClient)
                )

//                val traceStack = mutableListOf<Trace>()
//                if (traces != null) {
//                    traces.traces.forEach {
//                        val trace = Trace(
//                            segmentId = it.segmentId,
//                            operationNames = it.endpointNames,
//                            duration = it.duration,
//                            start = it.start.toLong(),
//                            error = it.isError,
//                            traceIds = it.traceIds,
//                            prettyDuration = "10s" //todo: generated from duration
//                        )
//
//                        trace.traceIds.forEach { traceId ->
//                            val traceResult = skywalkingClient.queryTraceStack(traceId = traceId)
//                            if (traceResult != null) {
//                                val spanStack = traceResult.toProtocol()
//                                //traceResult.traceStack.add(traceResult.)
//                            }
//                        }
//                    }
//                }

                val traceStack = mutableListOf<Trace>()
                if (traces != null) {
                    traceStack.addAll(traces.traces.map { it.toProtocol() })
                }
                it.reply(
                    TraceResult(
                        appUuid = request.appUuid,
                        artifactQualifiedName = request.artifactQualifiedName,
                        start = request.zonedDuration.start.toInstant().toEpochMilli(),
                        stop = request.zonedDuration.start.toInstant().toEpochMilli(),
                        total = traceStack.size,
                        traces = traceStack,
                        orderType = TraceOrderType.LATEST_TRACES
                    )
                )
            }
        }
        vertx.eventBus().localConsumer<String>("$address.getTraceStack") {
            launch(vertx.dispatcher()) {
                val traceStack = skywalkingClient.queryTraceStack(it.body())
                if (traceStack != null) {
                    it.reply(
                        TraceSpanStackQueryResult(
                            traceSpans = traceStack.spans.map { it.toProtocol() },
                            total = traceStack.spans.size
                        )
                    )
                } else {
                    it.reply(null)
                }
            }
        }
    }

    companion object {
        private const val address = "monitor.skywalking.endpoint.traces"

        suspend fun getTraces(request: GetEndpointTraces, vertx: Vertx): TraceResult {
            return vertx.eventBus()
                .requestAwait<TraceResult>("$address.getTraces", request)
                .body()
        }

        suspend fun getTraceStack(traceId: String, vertx: Vertx): TraceSpanStackQueryResult {
            return vertx.eventBus()
                .requestAwait<TraceSpanStackQueryResult>("$address.getTraceStack", traceId)
                .body()
        }
    }
}