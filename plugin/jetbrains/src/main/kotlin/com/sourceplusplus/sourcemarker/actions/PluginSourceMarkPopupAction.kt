package com.sourceplusplus.sourcemarker.actions

import com.intellij.openapi.editor.Editor
import com.sourceplusplus.marker.source.mark.SourceMarkPopupAction
import com.sourceplusplus.marker.source.mark.api.MethodSourceMark
import com.sourceplusplus.marker.source.mark.api.SourceMark
import com.sourceplusplus.marker.source.mark.api.component.jcef.SourceMarkJcefComponent
import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import com.sourceplusplus.monitor.skywalking.model.GetEndpointMetrics
import com.sourceplusplus.monitor.skywalking.model.ZonedDuration
import com.sourceplusplus.monitor.skywalking.track.EndpointMetricsTracker
import com.sourceplusplus.monitor.skywalking.track.EndpointTracker
import com.sourceplusplus.monitor.skywalking.track.toDoubleArray
import com.sourceplusplus.portal.server.model.MetricType
import com.sourceplusplus.portal.server.model.QueryTimeFrame
import com.sourceplusplus.portal.server.model.SplineChart
import com.sourceplusplus.portal.server.model.SplineSeriesData
import com.sourceplusplus.sourcemarker.activities.PluginSourceMarkerStartupActivity
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.uast.expressions.UInjectionHost
import org.jetbrains.uast.java.JavaUQualifiedReferenceExpression
import java.time.ZonedDateTime
import java.util.concurrent.ThreadLocalRandom

class PluginSourceMarkPopupAction : SourceMarkPopupAction() {

    override fun performPopupAction(sourceMark: SourceMark, editor: Editor) {
        //todo: determine sourceportal context
        //context = controller
        //todo: get all endpoint keys for current file
        //todo: save endpoint keys to sourcemark

        //context = endpoint
        val endpointName = determineEndpointName(sourceMark)
        if (endpointName != null) {
            GlobalScope.launch(PluginSourceMarkerStartupActivity.vertx.dispatcher()) {
                val endpoint =
                    EndpointTracker.searchExactEndpoint(endpointName, PluginSourceMarkerStartupActivity.vertx)

                //todo: portal should request chart
                PluginSourceMarkerStartupActivity.vertx.setPeriodic(3000) {
                    GlobalScope.launch(PluginSourceMarkerStartupActivity.vertx.dispatcher()) {
                        val metricsRequest = GetEndpointMetrics(
                            listOf("endpoint_cpm", "endpoint_avg", "endpoint_sla", "endpoint_percentile"),
                            endpoint!!.id,
                            ZonedDuration(
                                ZonedDateTime.now().minusMinutes(15),
                                ZonedDateTime.now(),
                                SkywalkingClient.DurationStep.MINUTE
                            )
                        )
                        val metrics = EndpointMetricsTracker.getMetrics(
                            metricsRequest, PluginSourceMarkerStartupActivity.vertx
                        )

                        val seriesData =
                            SplineSeriesData(0, metricsRequest.toInstantTimes(), metrics[0].toDoubleArray())
                        val splineChart =
                            SplineChart(
                                MetricType.ResponseTime_Average,
                                QueryTimeFrame.LAST_15_MINUTES,
                                listOf(seriesData)
                            )
                        PluginSourceMarkerStartupActivity.vertx.eventBus()
                            .publish("null-UpdateChart", JsonObject(Json.encode(splineChart)))
                    }
                }
            }
        }

        //todo: determine endpoint key
        //todo: save endpoint key to sourcemark

        //todo: use SourcePortalAPI to ensure correct view is showing
        val jcefComponent = sourceMark.sourceMarkComponent as SourceMarkJcefComponent
        if (ThreadLocalRandom.current().nextBoolean()) {
            jcefComponent.getBrowser().cefBrowser.executeJavaScript(
                """
                  window.location.href = 'http://localhost:8080/configuration';
            """.trimIndent(), "", 0
            )
        } else {
            jcefComponent.getBrowser().cefBrowser.executeJavaScript(
                """
                  window.location.href = 'http://localhost:8080/traces';
            """.trimIndent(), "", 0
            )
        }

        super.performPopupAction(sourceMark, editor)
    }

    private fun determineEndpointName(sourceMark: SourceMark): String? {
        if (sourceMark is MethodSourceMark) {
            val requestMappingAnnotation =
                sourceMark.getPsiMethod().findAnnotation("org.springframework.web.bind.annotation.RequestMapping")
            if (requestMappingAnnotation != null) {
                val value = (requestMappingAnnotation.findAttributeValue("value") as UInjectionHost).evaluateToString()
                val method =
                    (requestMappingAnnotation.findAttributeValue("method") as JavaUQualifiedReferenceExpression).selector
                return "{${method}}$value"
            }
        }
        return null
    }
}