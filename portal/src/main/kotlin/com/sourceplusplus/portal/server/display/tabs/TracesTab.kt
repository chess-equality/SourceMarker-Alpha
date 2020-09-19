package com.sourceplusplus.portal.server.display.tabs

import com.sourceplusplus.portal.extensions.displaySpanInfo
import com.sourceplusplus.portal.extensions.displayTraceStack
import com.sourceplusplus.portal.extensions.displayTraces
import com.sourceplusplus.portal.server.display.PortalTab
import com.sourceplusplus.portal.server.display.SourcePortal
import com.sourceplusplus.portal.server.display.tabs.views.TracesView
import com.sourceplusplus.protocol.ArtifactNameUtils.getShortQualifiedFunctionName
import com.sourceplusplus.protocol.ArtifactNameUtils.removePackageAndClassName
import com.sourceplusplus.protocol.ArtifactNameUtils.removePackageNames
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.ClickedDisplaySpanInfo
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.ClickedDisplayTraceStack
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.ClickedDisplayTraces
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.TracesTabOpened
import com.sourceplusplus.protocol.artifact.trace.*
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.regex.Pattern

/**
 * Displays traces (and the underlying spans) for a given source code artifact.
 *
 * @version 0.3.2
 * @since 0.1.0
 * @author <a href="mailto:brandon@srcpl.us">Brandon Fergerson</a>
 */
class TracesTab : AbstractTab(PortalTab.Traces) {

    companion object {
        val GET_TRACE_STACK = "GetTraceStack"
        val QUALIFIED_NAME_PATTERN = Pattern.compile(".+\\..+\\(.*\\)")

        private val log = LoggerFactory.getLogger(TracesTab::class.java)
    }

    override fun start() {
        super.start()

        //refresh with traces from cache (if avail)
        vertx.eventBus().consumer<JsonObject>(TracesTabOpened) {
            log.info("Traces tab opened")
            val message = JsonObject.mapFrom(it.body())
            val portalUuid = message.getString("portal_uuid")
            val portal = SourcePortal.getPortal(portalUuid)
            if (portal == null) {
                log.warn("Ignoring traces tab opened event. Unable to find portal: {}", portalUuid)
                return@consumer
            }

            val orderType = message.getString("trace_order_type")
            if (orderType != null) {
                //user possibly changed current trace order type; todo: create event
                portal.portalUI.tracesView.orderType = TraceOrderType.valueOf(orderType.toUpperCase())
            }
            portal.portalUI.currentTab = PortalTab.Traces
            SourcePortal.ensurePortalActive(portal)
            updateUI(portal)

            //subscribe (re-subscribe) to get traces as they are created
//            val subscribeRequest = ArtifactTraceSubscribeRequest.builder()
//                    .appUuid(portal.appUuid)
//                    .artifactQualifiedName(portal.portalUI.viewingPortalArtifact)
//                    .addOrderTypes(portal.portalUI.tracesView.orderType)
//                    .timeFrame(QueryTimeFrame.LAST_5_MINUTES)
//                    .build()
//            SourcePortalConfig.current.getCoreClient(portal.appUuid).subscribeToArtifact(subscribeRequest, {
//                if (it.succeeded()) {
//                    log.info("Successfully subscribed to traces with request: {}", subscribeRequest)
//                } else {
//                    log.error("Failed to subscribe to artifact traces", it.cause())
//                }
//            })
        }
//        vertx.eventBus().consumer<TraceResult>(ARTIFACT_TRACE_UPDATED.address) {
//            handleArtifactTraceResult(it.body())
//        }

        //get historical traces
        vertx.eventBus().consumer<JsonObject>(TracesTabOpened) {
            val portalUuid = JsonObject.mapFrom(it.body()).getString("portal_uuid")
            val portal = SourcePortal.getPortal(portalUuid)
            if (portal == null) {
                log.warn("Ignoring traces tab opened event. Unable to find portal: {}", portalUuid)
            } else {
                if (portal.external) {
                    portal.portalUI.tracesView.viewTraceAmount = 25
                }

//                val traceQuery = TraceQuery.builder()
//                    .orderType(portal.portalUI.tracesView.orderType)
//                    .pageSize(portal.portalUI.tracesView.viewTraceAmount)
//                    .appUuid(portal.appUuid)
//                    .artifactQualifiedName(portal.portalUI.viewingPortalArtifact)
//                    .durationStart(Instant.now().minus(30, ChronoUnit.DAYS))
//                    .durationStop(Instant.now())
//                    .durationStep("SECOND").build()
//                SourcePortalConfig.current.getCoreClient(portal.appUuid).getTraces(traceQuery, {
//                    if (it.succeeded()) {
//                        def traceResult = ArtifactTraceResult.builder()
//                                .appUuid(traceQuery.appUuid())
//                                .artifactQualifiedName(traceQuery.artifactQualifiedName())
//                                .orderType(traceQuery.orderType())
//                                .start(traceQuery.durationStart())
//                                .stop(traceQuery.durationStop())
//                                .step(traceQuery.durationStep())
//                                .traces(it.result().traces())
//                                .total(it.result().total())
//                                .build()
//                        handleArtifactTraceResult(Collections.singletonList(portal), traceResult)
//                    } else {
//                        log.error("Failed to get traces", it.cause())
//                    }
//                })
            }
        }

        //user viewing portal under new artifact
//        vertx.eventBus().consumer(PortalViewTracker.CHANGED_PORTAL_ARTIFACT, {
////            def portal = SourcePortal.getPortal(JsonObject.mapFrom(it.body()).getString("portal_uuid"))
////            vertx.eventBus().send(portal.portalUuid + "-ClearTraceStack", new JsonObject())
//        })

        //user clicked into trace stack
        vertx.eventBus().consumer<JsonObject>(ClickedDisplayTraceStack) { messageHandler ->
            val request = messageHandler.body() as JsonObject
            log.debug("Displaying trace stack: {}", request)

            if (request.getString("trace_id") == null) {
                val portal = SourcePortal.getPortal(request.getString("portal_uuid"))!!
                portal.portalUI.tracesView.viewType = TracesView.Companion.ViewType.TRACE_STACK
                updateUI(portal)
            } else {
                vertx.eventBus().request<JsonArray>(GET_TRACE_STACK, request) {
                    if (it.failed()) {
                        it.cause().printStackTrace()
                        log.error("Failed to display trace stack", it.cause())
                    } else {
                        val portal = SourcePortal.getPortal(request.getString("portal_uuid"))!!
                        portal.portalUI.tracesView.viewType = TracesView.Companion.ViewType.TRACE_STACK
                        portal.portalUI.tracesView.traceStack = it.result().body() as JsonArray
                        portal.portalUI.tracesView.traceId = request.getString("trace_id")
                        updateUI(portal)
                    }
                }
            }
        }

        vertx.eventBus().consumer<JsonObject>(ClickedDisplayTraces) {
            val portal = SourcePortal.getPortal((it.body() as JsonObject).getString("portal_uuid"))!!
            val representation = portal.portalUI.tracesView
            representation.viewType = TracesView.Companion.ViewType.TRACES

            if (representation.innerTraceStack.size > 0) {
                representation.viewType = TracesView.Companion.ViewType.TRACE_STACK
                val stack = representation.innerTraceStack.pop()

                if (representation.innerTrace) {
                    updateUI(portal)
                } else if (!portal.external) {
                    //navigating back to parent stack
//                    val rootArtifactQualifiedName = stack.getJsonObject(0).getString("root_artifact_qualified_name")
//                    vertx.eventBus().send(
//                        NAVIGATE_TO_ARTIFACT.address,
//                        JsonObject().put("portal_uuid", portal.portalUuid)
//                            .put("artifact_qualified_name", rootArtifactQualifiedName)
//                            .put("parent_stack_navigation", true)
//                    )
                } else {
                    updateUI(portal)
                }
            } else {
                updateUI(portal)
            }
        }

        //user clicked into span
        vertx.eventBus().consumer<JsonObject>(ClickedDisplaySpanInfo) { messageHandler ->
            val spanInfoRequest = messageHandler.body() as JsonObject
            log.debug("Clicked display span info: {}", spanInfoRequest)

            val portalUuid = spanInfoRequest.getString("portal_uuid")
            val portal = SourcePortal.getPortal(portalUuid)!!
            val representation = portal.portalUI.tracesView
            representation.viewType = TracesView.Companion.ViewType.SPAN_INFO
            representation.traceId = spanInfoRequest.getString("trace_id")
            representation.spanId = spanInfoRequest.getInteger("span_id")
            updateUI(portal)
        }

        //query core for trace stack (or get from cache)
        vertx.eventBus().consumer<JsonObject>(GET_TRACE_STACK) { messageHandler ->
//            val timer = PortalBootstrap.portalMetrics.timer(GET_TRACE_STACK)
//            val context = timer.time()
            val request = messageHandler.body() as JsonObject
            val portalUuid = request.getString("portal_uuid")
            val appUuid = request.getString("app_uuid")
            val artifactQualifiedName = request.getString("artifact_qualified_name")
            val globalTraceId = request.getString("trace_id")
            log.trace(
                "Getting trace spans. Artifact qualified name: {} - Trace id: {}",
                getShortQualifiedFunctionName(artifactQualifiedName), globalTraceId
            )

            val portal = SourcePortal.getPortal(portalUuid)!!
            val representation = portal.portalUI.tracesView
            val traceStack = representation.getTraceStack(globalTraceId)
            if (traceStack != null) {
                log.trace("Got trace spans: {} from cache - Stack size: {}", globalTraceId, traceStack.size())
                messageHandler.reply(traceStack)
//                context.stop()
            } else {
//                val traceStackQuery = TraceSpanStackQuery.builder()
//                    .oneLevelDeep(true)
//                    .traceId(globalTraceId).build()
//                SourcePortalConfig.current.getCoreClient(appUuid)
//                    .getTraceSpans(appUuid, artifactQualifiedName, traceStackQuery) {
//                        if (it.failed()) {
////                        log.error("Failed to get trace spans", it.cause())
//                        } else {
//                            representation.cacheTraceStack(
//                                globalTraceId, handleTraceStack(
//                                    appUuid, artifactQualifiedName, it.result()
//                                )
//                            )
//                            messageHandler.reply(representation.getTraceStack(globalTraceId))
////                        context.stop()
//                        }
//                    }
            }
        }
        log.info("{} started", javaClass.simpleName)
    }

    override fun updateUI(portal: SourcePortal) {
        if (portal.portalUI.currentTab != thisTab) {
            return
        }

        when (portal.portalUI.tracesView.viewType) {
            TracesView.Companion.ViewType.TRACES -> displayTraces(portal)
            TracesView.Companion.ViewType.TRACE_STACK -> displayTraceStack(portal)
            TracesView.Companion.ViewType.SPAN_INFO -> displaySpanInfo(portal)
        }
    }

    fun displayTraces(portal: SourcePortal) {
        if (portal.portalUI.tracesView.artifactTraceResult != null) {
            val artifactTraceResult = portal.portalUI.tracesView.artifactTraceResult!!
            vertx.eventBus().displayTraces(portal.portalUuid, artifactTraceResult)
            log.debug(
                "Displayed traces for artifact: {} - Type: {} - Trace size: {}",
                getShortQualifiedFunctionName(artifactTraceResult.artifactQualifiedName),
                artifactTraceResult.orderType, artifactTraceResult.traces.size
            )
        }
    }

    fun displayTraceStack(portal: SourcePortal) {
        val representation = portal.portalUI.tracesView
        val traceId = representation.traceId
        val traceStack = representation.traceStack

        if (representation.innerTrace && representation.viewType != TracesView.Companion.ViewType.SPAN_INFO) {
            val innerTraceStackInfo = InnerTraceStackInfo(
                innerLevel = representation.innerTraceStack.size,
                traceStack = representation.innerTraceStack.peek().toString()
            )
            vertx.eventBus().publish(
                portal.portalUuid + "-DisplayInnerTraceStack",
                JsonObject(Json.encode(innerTraceStackInfo))
            )
            log.info("Displayed inner trace stack. Stack size: {}", representation.innerTraceStack.peek().size())
        } else if (traceStack != null && !traceStack.isEmpty) {
            vertx.eventBus().displayTraceStack(portal.portalUuid, representation.traceStack!!)
            log.info("Displayed trace stack for id: {} - Stack size: {}", traceId, traceStack.size())
        }
    }

    fun displaySpanInfo(portal: SourcePortal) {
        val traceId = portal.portalUI.tracesView.traceId!!
        val spanId = portal.portalUI.tracesView.spanId
        val representation = portal.portalUI.tracesView
        val traceStack = if (representation.innerTrace) {
            representation.innerTraceStack.peek()
        } else {
            representation.getTraceStack(traceId)!!
        }

        for (i in 0 until traceStack.size()) {
            val span = traceStack.getJsonObject(i).getJsonObject("span")
            if (span.getInteger("span_id") == spanId) {
                val spanArtifactQualifiedName = span.getString("artifact_qualified_name")
                if (portal.external && span.getBoolean("has_child_stack")) {
//                    val spanStackQuery = TraceSpanStackQuery.builder()
//                        .oneLevelDeep(true).followExit(true)
//                        .segmentId(span.getString("segment_id"))
//                        .spanId(span.getLong("span_id"))
//                        .traceId(traceId).build()
//                    SourcePortalConfig.current.getCoreClient(portal.appUuid).getTraceSpans(portal.appUuid,
//                        portal.portalUI.viewingPortalArtifact, spanStackQuery, {
//                            if (it.failed()) {
////                                log.error("Failed to get trace spans", it.cause())
//                                vertx.eventBus().send(portal.portalUuid + "-$DISPLAY_SPAN_INFO", span)
//                            } else {
//                                val queryResult = it.result()
//                                val spanTracesView = portal.portalUI.tracesView
//                                        spanTracesView.viewType = TracesView.Companion.ViewType.TRACE_STACK
//                                spanTracesView.innerTraceStack.push(
//                                    handleTraceStack(
//                                        portal.appUuid, portal.portalUI.viewingPortalArtifact, queryResult
//                                    )
//                                )
//
//                                displayTraceStack(portal)
//                            }
//                        })
                    break
                } else if (spanArtifactQualifiedName == null ||
                    spanArtifactQualifiedName == portal.portalUI.viewingPortalArtifact
                ) {
                    vertx.eventBus().displaySpanInfo(portal.portalUuid, span)
                    log.info("Displayed trace span info: {}", span)
                } else {
//                    vertx.eventBus().request<Boolean>(
//                        CAN_NAVIGATE_TO_ARTIFACT.address, JsonObject()
//                            .put("app_uuid", portal.appUuid)
//                            .put("artifact_qualified_name", spanArtifactQualifiedName)
//                    ) {
//                        if (it.succeeded() && it.result().body() == true) {
//                            val spanStackQuery = TraceSpanStackQuery.builder()
//                                .oneLevelDeep(true).followExit(true)
//                                .segmentId(span.getString("segment_id"))
//                                .spanId(span.getLong("span_id"))
//                                .traceId(traceId).build()
//
//                            val spanPortal = SourcePortal.getInternalPortal(portal.appUuid, spanArtifactQualifiedName)
//                            if (!spanPortal.isPresent) {
////                                log.error("Failed to get span portal: {}", spanArtifactQualifiedName)
//                                vertx.eventBus().send(portal.portalUuid + "-$DISPLAY_SPAN_INFO", span)
//                                return@request
//                            }
//
//                            //todo: cache
//                            SourcePortalConfig.current.getCoreClient(portal.appUuid).getTraceSpans(
//                                portal.appUuid,
//                                portal.portalUI.viewingPortalArtifact, spanStackQuery
//                            ) {
//                                if (it.failed()) {
////                                        log.error("Failed to get trace spans", it.cause())
//                                    vertx.eventBus().send(portal.portalUuid + "-$DISPLAY_SPAN_INFO", span)
//                                } else {
//                                    //navigated away from portal; reset to trace stack
//                                    portal.portalUI.tracesView.viewType = TracesView.Companion.ViewType.TRACE_STACK
//
//                                    val queryResult = it.result()
//                                    val spanTracesView = spanPortal.get().portalUI.tracesView
//                                    spanTracesView.viewType = TracesView.Companion.ViewType.TRACE_STACK
//                                    spanTracesView.innerTraceStack.push(
//                                        handleTraceStack(
//                                            portal.appUuid, portal.portalUI.viewingPortalArtifact, queryResult
//                                        )
//                                    )
//                                    vertx.eventBus().send(
//                                        NAVIGATE_TO_ARTIFACT.address,
//                                        JsonObject().put("portal_uuid", spanPortal.get().portalUuid)
//                                            .put("artifact_qualified_name", spanArtifactQualifiedName)
//                                    )
//                                }
//                            }
//                        } else {
//                            vertx.eventBus().send(portal.portalUuid + "-$DISPLAY_SPAN_INFO", span)
////                            log.info("Displayed trace span info: {}", span)
//                        }
//                    }
                }
            }
        }
    }

    fun handleArtifactTraceResult(artifactTraceResult: TraceResult) {
        handleArtifactTraceResult(
            SourcePortal.getPortals(
                artifactTraceResult.appUuid,
                artifactTraceResult.artifactQualifiedName
            ).toList(), artifactTraceResult
        )
    }

    fun handleArtifactTraceResult(portals: List<SourcePortal>, artifactTraceResult: TraceResult) {
        val traces = ArrayList<Trace>()
        artifactTraceResult.traces.forEach {
            traces.add(it.copy(prettyDuration = humanReadableDuration(Duration.ofMillis(it.duration.toLong()))))
        }
        val updatedArtifactTraceResult = artifactTraceResult.copy(
            traces = traces,
            artifactSimpleName = removePackageAndClassName(removePackageNames(artifactTraceResult.artifactQualifiedName))
        )

        portals.forEach {
            val representation = it.portalUI.tracesView
            representation.cacheArtifactTraceResult(updatedArtifactTraceResult)

            if (it.portalUI.viewingPortalArtifact == updatedArtifactTraceResult.artifactQualifiedName
                && it.portalUI.tracesView.viewType == TracesView.Companion.ViewType.TRACES
            ) {
                updateUI(it)
            }
        }
    }

    fun handleTraceStack(
        appUuid: String, rootArtifactQualifiedName: String,
        spanQueryResult: TraceSpanStackQueryResult
    ): JsonArray {
        val spanInfos = ArrayList<TraceSpanInfo>()
        val totalTime = spanQueryResult.traceSpans[0].endTime!! - spanQueryResult.traceSpans[0].startTime

        spanQueryResult.traceSpans.forEach { span ->
            val timeTookMs = span.endTime!! - span.startTime
            val timeTook = humanReadableDuration(Duration.ofMillis(timeTookMs))

            //detect if operation name is really an artifact name
//            val operationName = ""
//            if (QUALIFIED_NAME_PATTERN.matcher(span.endpointName).matches()) {
//                spanInfo.span(span = span.withArtifactQualifiedName(span.endpointName()))
//            }
//            if (span.artifactQualifiedName) {
//                spanInfo.operationName(removePackageAndClassName(removePackageNames(span.artifactQualifiedName())))
//            } else {
//                spanInfo.operationName(span.endpointName())
//            }

            spanInfos.add(
                TraceSpanInfo(
                    span = span,
                    appUuid = appUuid,
                    rootArtifactQualifiedName = rootArtifactQualifiedName,
                    timeTook = timeTook,
                    totalTracePercent = if (totalTime == 0L) {
                        0.0
                    } else {
                        timeTookMs / totalTime * 100.0
                    }
                )
            )
        }
        return JsonArray(Json.encode(spanInfos))
    }

    fun humanReadableDuration(duration: Duration): String {
        if (duration.seconds < 1) {
            return duration.toMillis().toString() + "ms"
        }
        return duration.toString().substring(2)
            .replace("(\\d[HMS])(?!$)", "$1 ")
            .toLowerCase()
    }

//    private fun updateTraces(endpointId: String) {
//        GlobalScope.launch(vertx.dispatcher()) {
//            val traceResult = EndpointTracesTracker.getTraces(
//                GetEndpointTraces(
//                    endpointId = endpointId,
//                    zonedDuration = ZonedDuration(
//                        ZonedDateTime.now().minusMinutes(15),
//                        ZonedDateTime.now(),
//                        SkywalkingClient.DurationStep.MINUTE
//                    )
//                ), vertx
//            )
//            vertx.eventBus().publish(ProtocolAddress.Portal.DisplayTraces("null"), JsonObject(Json.encode(traceResult)))
//        }
//    }
}
