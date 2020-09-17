package com.sourceplusplus.portal.server

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.guava.GuavaModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.sourceplusplus.portal.server.model.*
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.ext.bridge.PermittedOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import java.time.Instant
import java.util.*
import java.util.concurrent.ThreadLocalRandom.current

fun main() {
    DatabindCodec.mapper().registerModule(GuavaModule())
    DatabindCodec.mapper().registerModule(Jdk8Module())
    DatabindCodec.mapper().registerModule(JavaTimeModule())
    DatabindCodec.mapper().propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
    DatabindCodec.mapper().enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
    DatabindCodec.mapper().enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)

    val vertx = Vertx.vertx()
    val sockJSHandler = SockJSHandler.create(vertx)
    val portalBridgeOptions = SockJSBridgeOptions()
        .addInboundPermitted(PermittedOptions().setAddressRegex(".+"))
        .addOutboundPermitted(PermittedOptions().setAddressRegex(".+"))
    sockJSHandler.bridge(portalBridgeOptions)

    val router = Router.router(vertx)
    router.route("/eventbus/*").handler(sockJSHandler)

    vertx.createHttpServer().requestHandler(router).listen(8888, "localhost")

    vertx.eventBus().consumer<String>("ClickedViewAsExternalPortal") {
        it.reply(JsonObject().put("portal_uuid", "1"))
    }

    vertx.eventBus().consumer<Void>("OverviewTabOpened") {
        updateCards(vertx)

        vertx.eventBus().publish("1-ClearOverview", "")
        displayChart(vertx)
    }
    vertx.setPeriodic(2500) {
        updateCards(vertx)
        displayChart(vertx)
    }

    vertx.eventBus().consumer<Void>("ClickedDisplayTraces") {
        displayTraces(vertx)
    }
    vertx.eventBus().consumer<Void>("TracesTabOpened") {
        displayTraces(vertx)
    }

    vertx.eventBus().consumer<String>("ClickedDisplayTraceStack") {
        val traceStack = JsonArray()
        for (i in 1..5) {
            traceStack.add(
                JsonObject().put("root_artifact_qualified_name", UUID.randomUUID().toString())
                    .put("app_uuid", "1")
                    .put("operation_name", UUID.randomUUID().toString())
                    .put("time_took", "10s")
                    .put("total_trace_percent", current().nextInt(100))
                    .put(
                        "span", JsonObject()
                            .put("error", current().nextBoolean())
                            .put("artifact_qualified_name", UUID.randomUUID().toString())
                            .put("has_child_stack", false)
                            .put("trace_id", "100")
                            .put("segment_id", "100")
                            .put("span_id", "100")
                            .put("start_time", Instant.now().toEpochMilli())
                            .put("component", "DATABASE")
                    )
            )
        }
        vertx.eventBus().publish("1-DisplayTraceStack", traceStack)
    }

    vertx.eventBus().consumer<Void>("ClickedDisplaySpanInfo") {
        vertx.eventBus().publish(
            "1-DisplaySpanInfo", JsonObject()
                .put("start_time", Instant.now().toEpochMilli())
                .put("end_time", Instant.now().toEpochMilli())
                .put("segment_id", "100")
                .put(
                    "tags", JsonObject()
                        .put("thing1", UUID.randomUUID().toString())
                        .put("thing2", UUID.randomUUID().toString())
                        .put("thing3", UUID.randomUUID().toString())
                        .put("thing4", UUID.randomUUID().toString())
                        .put("thing5", UUID.randomUUID().toString())
                )
                .put(
                    "logs", JsonArray().add(
                        JsonObject()
                            .put("data", UUID.randomUUID().toString())
                            .put("time", Instant.now().epochSecond)
                    )
                )
        )
    }

    vertx.eventBus().consumer<Void>("ConfigurationTabOpened") {
        vertx.eventBus().publish(
            "1-DisplayArtifactConfiguration", JsonObject()
                .put("artifact_qualified_name", UUID.randomUUID().toString())
                .put("create_date", Instant.now().epochSecond)
                .put("last_updated", Instant.now().epochSecond)
                .put(
                    "config", JsonObject()
                        .put("endpoint", current().nextBoolean())
                        .put("subscribe_automatically", current().nextBoolean())
                        .put("endpoint_name", UUID.randomUUID().toString())
                )
        )
    }
}

fun displayChart(vertx: Vertx) {
    val seriesData =
        SplineSeriesData(
            0, listOf(Instant.now(), Instant.now().plusSeconds(10)),
            doubleArrayOf(current().nextDouble(10.0), current().nextDouble(10.0))
        )
    val splineChart = SplineChart(MetricType.ResponseTime_Average, QueryTimeFrame.LAST_15_MINUTES, listOf(seriesData))
    vertx.eventBus().publish("1-UpdateChart", JsonObject(Json.encode(splineChart)))
}

fun displayTraces(vertx: Vertx) {
    val traces = JsonArray()
    for (i in 1..20) {
        traces.add(
            JsonObject().put(
                "trace_ids",
                JsonArray().add(current().nextInt().toString() + "." + current().nextInt().toString())
            )
                .put("operation_names", JsonArray().add(UUID.randomUUID().toString()))
                .put("pretty_duration", "10s")
                .put("error", current().nextBoolean())
                .put("start", Instant.now().toEpochMilli())
        )
    }
    vertx.eventBus().publish(
        "1-DisplayTraces", JsonObject()
            .put("app_uuid", "1")
            .put("artifact_qualified_name", UUID.randomUUID().toString())
            .put("artifact_simple_name", UUID.randomUUID().toString())
            .put("start", Instant.now().epochSecond)
            .put("stop", Instant.now().epochSecond)
            .put("total", 20)
            .put("order_type", "LATEST_TRACES")
            .put("traces", traces)
    )
}

fun updateCards(vertx: Vertx) {
    val throughputAverageCard =
        BarTrendCard(meta = "throughput_average", header = current().nextInt(100).toString())
    val responseTimeAverageCard =
        BarTrendCard(meta = "responsetime_average", header = current().nextInt(100).toString())
    val slaAverageCard =
        BarTrendCard(meta = "servicelevelagreement_average", header = current().nextInt(100).toString())
    vertx.eventBus().publish("1-DisplayCard", JsonObject(Json.encode(throughputAverageCard)))
    vertx.eventBus().publish("1-DisplayCard", JsonObject(Json.encode(responseTimeAverageCard)))
    vertx.eventBus().publish("1-DisplayCard", JsonObject(Json.encode(slaAverageCard)))
}
