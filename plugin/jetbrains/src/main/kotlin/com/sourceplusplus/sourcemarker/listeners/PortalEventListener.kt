package com.sourceplusplus.sourcemarker.listeners

import com.intellij.openapi.application.ApplicationManager
import com.sourceplusplus.marker.plugin.SourceMarkerPlugin
import com.sourceplusplus.marker.source.mark.api.SourceMark
import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import com.sourceplusplus.monitor.skywalking.model.GetEndpointMetrics
import com.sourceplusplus.monitor.skywalking.model.ZonedDuration
import com.sourceplusplus.monitor.skywalking.toProtocol
import com.sourceplusplus.monitor.skywalking.track.EndpointMetricsTracker
import com.sourceplusplus.portal.server.display.SourcePortal
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.ArtifactMetricUpdated
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.ClosePortal
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.RefreshOverview
import com.sourceplusplus.sourcemarker.actions.PluginSourceMarkPopupAction.Companion.ENDPOINT_ID
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class PortalEventListener : CoroutineVerticle() {

    override suspend fun start() {
        vertx.eventBus().consumer<SourcePortal>(ClosePortal) {
            val sourceMark =
                SourceMarkerPlugin.getSourceMark(it.body().viewingPortalArtifact, SourceMark.Type.GUTTER)
            if (sourceMark != null) {
                ApplicationManager.getApplication().invokeLater(sourceMark::closePopup)
            }
        }

        vertx.eventBus().consumer<SourcePortal>(RefreshOverview) {
            val sourcePortal = it.body()
            val sourceMark =
                SourceMarkerPlugin.getSourceMark(sourcePortal.viewingPortalArtifact, SourceMark.Type.GUTTER)
            if (sourceMark?.getUserData(ENDPOINT_ID) != null) {
                val endpointId = sourceMark.getUserData(ENDPOINT_ID)!!
                GlobalScope.launch(vertx.dispatcher()) {
                    val metricsRequest = GetEndpointMetrics(
                        listOf("endpoint_cpm", "endpoint_avg", "endpoint_sla", "endpoint_percentile"),
                        endpointId,
                        ZonedDuration(
                            ZonedDateTime.now().minusMinutes(sourcePortal.overviewView.timeFrame.minutes.toLong()),
                            ZonedDateTime.now(),
                            SkywalkingClient.DurationStep.MINUTE
                        )
                    )
                    val metrics = EndpointMetricsTracker.getMetrics(metricsRequest, vertx)
                    val metricResult = toProtocol(
                        sourcePortal.appUuid,
                        sourcePortal.viewingPortalArtifact,
                        sourcePortal.overviewView.timeFrame,
                        metricsRequest,
                        metrics
                    )
                    vertx.eventBus().send(ArtifactMetricUpdated, metricResult)
                }
            }
        }
    }
}