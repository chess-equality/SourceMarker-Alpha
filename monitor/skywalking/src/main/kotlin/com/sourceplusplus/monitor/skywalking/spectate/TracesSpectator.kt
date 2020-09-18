package com.sourceplusplus.monitor.skywalking.spectate

import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import com.sourceplusplus.monitor.skywalking.model.GetEndpointTraces
import com.sourceplusplus.monitor.skywalking.model.ZonedDuration
import com.sourceplusplus.monitor.skywalking.track.EndpointTracesTracker
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.TracesTabOpened
import com.sourceplusplus.protocol.ProtocolAddress.Portal.Companion.DisplayTraces
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class TracesSpectator : CoroutineVerticle() {

    override suspend fun start() {
        vertx.eventBus().localConsumer<JsonObject>(TracesTabOpened) {
            println("here")
            //todo: get sourcemark or get portal uuid & endpointId
        }
    }

    private fun updateTraces(endpointId: String) {
        GlobalScope.launch(vertx.dispatcher()) {
            val traceResult = EndpointTracesTracker.getTraces(
                GetEndpointTraces(
                    endpointId = endpointId,
                    zonedDuration = ZonedDuration(
                        ZonedDateTime.now().minusMinutes(15),
                        ZonedDateTime.now(),
                        SkywalkingClient.DurationStep.MINUTE
                    )
                ), vertx
            )
            vertx.eventBus().publish(DisplayTraces("null"), JsonObject(Json.encode(traceResult)))
        }
    }
}