package com.sourceplusplus.sourcemarker.actions

import com.intellij.openapi.editor.Editor
import com.sourceplusplus.marker.source.mark.SourceMarkPopupAction
import com.sourceplusplus.marker.source.mark.api.MethodSourceMark
import com.sourceplusplus.marker.source.mark.api.SourceMark
import com.sourceplusplus.marker.source.mark.api.component.jcef.SourceMarkJcefComponent
import com.sourceplusplus.monitor.skywalking.track.EndpointTracker
import com.sourceplusplus.sourcemarker.activities.PluginSourceMarkerStartupActivity
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.uast.expressions.UInjectionHost
import org.jetbrains.uast.java.JavaUQualifiedReferenceExpression
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
                println(endpoint)

//                EndpointMetricsTracker.getMetrics(endpoint.id)
//                val card = JsonObject().put("meta", "throughput_average")
//                    .put("header", "again-" + System.currentTimeMillis())
//                PluginSourceMarkerStartupActivity.vertx.eventBus().publish("DisplayCard", card)
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