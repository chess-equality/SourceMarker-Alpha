package com.sourceplusplus.monitor.skywalking.track

import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import com.sourceplusplus.monitor.skywalking.model.GetEndpointMetrics
import io.vertx.core.Vertx
import io.vertx.kotlin.core.eventbus.requestAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.launch
import monitor.skywalking.protocol.metrics.GetLinearIntValuesQuery

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class EndpointMetricsTracker(private val skywalkingClient: SkywalkingClient) : CoroutineVerticle() {

    override suspend fun start() {
        vertx.eventBus().localConsumer<GetEndpointMetrics>(getMetricsAddress) {
            launch(vertx.dispatcher()) {
                val request = it.body()
                val response: MutableList<GetLinearIntValuesQuery.Result> = ArrayList()
                request.metricIds.forEach {
                    val metric =
                        skywalkingClient.getEndpointMetrics(
                            it,
                            request.endpointId,
                            request.zonedDuration.toDuration(skywalkingClient)
                        )
                    if (metric != null) response.add(metric)
                }
                it.reply(response)
            }
        }
    }

    companion object {
        private const val rootAddress = "monitor.skywalking.endpoint.metrics"
        private const val getMetricsAddress = "$rootAddress.getMetrics"

        suspend fun getMetrics(request: GetEndpointMetrics, vertx: Vertx): List<GetLinearIntValuesQuery.Result> {
            return vertx.eventBus()
                .requestAwait<List<GetLinearIntValuesQuery.Result>>(getMetricsAddress, request)
                .body()
        }
    }
}
