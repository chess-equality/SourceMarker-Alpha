package com.sourceplusplus.sourcemarker.listeners

import com.intellij.openapi.application.ApplicationManager
import com.sourceplusplus.marker.plugin.SourceMarkerPlugin
import com.sourceplusplus.marker.source.mark.api.SourceMark
import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import com.sourceplusplus.monitor.skywalking.model.GetEndpointMetrics
import com.sourceplusplus.monitor.skywalking.model.GetEndpointTraces
import com.sourceplusplus.monitor.skywalking.model.ZonedDuration
import com.sourceplusplus.monitor.skywalking.toProtocol
import com.sourceplusplus.monitor.skywalking.track.EndpointMetricsTracker
import com.sourceplusplus.monitor.skywalking.track.EndpointTracesTracker
import com.sourceplusplus.portal.server.display.SourcePortal
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.ArtifactMetricUpdated
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.ArtifactTraceUpdated
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.ClosePortal
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.QueryTraceStack
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.RefreshOverview
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.RefreshTraces
import com.sourceplusplus.sourcemarker.actions.PluginSourceMarkPopupAction.Companion.ENDPOINT_ID
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class PortalEventListener : CoroutineVerticle() {

    override suspend fun start() {
        vertx.eventBus().consumer<SourcePortal>(ClosePortal) { closePortal(it.body()) }
        vertx.eventBus().consumer<SourcePortal>(RefreshOverview) { refreshOverview(it.body()) }
        vertx.eventBus().consumer<SourcePortal>(RefreshTraces) { refreshTraces(it.body()) }
        vertx.eventBus().consumer<String>(QueryTraceStack) { handler ->
            val traceId = handler.body()
            GlobalScope.launch(vertx.dispatcher()) {
                handler.reply(EndpointTracesTracker.getTraceStack(traceId, vertx))
            }
        }
    }

    private fun refreshTraces(portal: SourcePortal) {
        val sourceMark =
            SourceMarkerPlugin.getSourceMark(portal.viewingPortalArtifact, SourceMark.Type.GUTTER)
        if (sourceMark?.getUserData(ENDPOINT_ID) != null) {
            val endpointId = sourceMark.getUserData(ENDPOINT_ID)!!

            GlobalScope.launch(vertx.dispatcher()) {
                val traceResult = EndpointTracesTracker.getTraces(
                    GetEndpointTraces(
                        appUuid = portal.appUuid,
                        artifactQualifiedName = portal.viewingPortalArtifact,
                        endpointId = endpointId,
                        zonedDuration = ZonedDuration(
                            ZonedDateTime.now().minusMinutes(15),
                            ZonedDateTime.now(),
                            SkywalkingClient.DurationStep.MINUTE
                        )
                    ), vertx
                )
                vertx.eventBus().send(ArtifactTraceUpdated, traceResult)
            }
        }
    }

    private fun refreshOverview(portal: SourcePortal) {
        val sourceMark =
            SourceMarkerPlugin.getSourceMark(portal.viewingPortalArtifact, SourceMark.Type.GUTTER)
        if (sourceMark?.getUserData(ENDPOINT_ID) != null) {
            val endpointId = sourceMark.getUserData(ENDPOINT_ID)!!

            GlobalScope.launch(vertx.dispatcher()) {
                val metricsRequest = GetEndpointMetrics(
                    listOf("endpoint_cpm", "endpoint_avg", "endpoint_sla", "endpoint_percentile"),
                    endpointId,
                    ZonedDuration(
                        ZonedDateTime.now().minusMinutes(portal.overviewView.timeFrame.minutes.toLong()),
                        ZonedDateTime.now(),
                        SkywalkingClient.DurationStep.MINUTE
                    )
                )
                val metrics = EndpointMetricsTracker.getMetrics(metricsRequest, vertx)
                val metricResult = toProtocol(
                    portal.appUuid,
                    portal.viewingPortalArtifact,
                    portal.overviewView.timeFrame,
                    metricsRequest,
                    metrics
                )
                vertx.eventBus().send(ArtifactMetricUpdated, metricResult)
            }
        }
    }

    private fun closePortal(portal: SourcePortal) {
        val sourceMark = SourceMarkerPlugin.getSourceMark(
            portal.viewingPortalArtifact, SourceMark.Type.GUTTER
        )
        if (sourceMark != null) {
            ApplicationManager.getApplication().invokeLater(sourceMark::closePopup)
        }
    }
}
