package com.sourceplusplus.sourcemarker.actions

import com.intellij.openapi.editor.Editor
import com.sourceplusplus.marker.source.mark.SourceMarkPopupAction
import com.sourceplusplus.marker.source.mark.api.ClassSourceMark
import com.sourceplusplus.marker.source.mark.api.MethodSourceMark
import com.sourceplusplus.marker.source.mark.api.SourceMark
import com.sourceplusplus.marker.source.mark.api.component.jcef.SourceMarkJcefComponent
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventCode
import com.sourceplusplus.marker.source.mark.api.key.SourceKey
import com.sourceplusplus.monitor.skywalking.track.EndpointTracker
import com.sourceplusplus.portal.server.display.SourcePortal
import com.sourceplusplus.sourcemarker.activities.PluginSourceMarkerStartupActivity.Companion.vertx
import com.sourceplusplus.sourcemarker.psi.EndpointNameDetector
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

class PluginSourceMarkPopupAction : SourceMarkPopupAction() {

    companion object {
        private val log = LoggerFactory.getLogger(PluginSourceMarkPopupAction::class.java)
        val SOURCE_PORTAL = SourceKey<SourcePortal>("SOURCE_PORTAL")
        val ENDPOINT_ID = SourceKey<String>("ENDPOINT_ID")
    }

    private val endpointDetector = EndpointNameDetector()

    override fun performPopupAction(sourceMark: SourceMark, editor: Editor) {
        //register source portal (if necessary)
        if (sourceMark.getUserData(SOURCE_PORTAL) == null) {
            val sourcePortal = SourcePortal.getPortal(
                SourcePortal.register( //todo: appUuid
                    "null", sourceMark.artifactQualifiedName, false
                )
            )
            sourceMark.putUserData(SOURCE_PORTAL, sourcePortal)

            sourceMark.addEventListener { event ->
                if (event.eventCode == SourceMarkEventCode.MARK_REMOVED) {
                    event.sourceMark.getUserData(SOURCE_PORTAL)!!.close()
                }
            }
        }
        val sourcePortal = sourceMark.getUserData(SOURCE_PORTAL)!!

        //todo: determine sourceportal context
        when (sourceMark) {
            is ClassSourceMark -> performClassPopup(sourceMark)
            is MethodSourceMark -> performMethodPopup(sourceMark)
        }

        //todo: use SourcePortalAPI to ensure correct view is showing (don't refresh if correct already viewing)
        //todo: likely need to unregister old portal handlers
        val jcefComponent = sourceMark.sourceMarkComponent as SourceMarkJcefComponent
//        if (ThreadLocalRandom.current().nextBoolean()) {
        jcefComponent.getBrowser().cefBrowser.executeJavaScript(
            """
                  window.location.href = 'http://localhost:8080/overview?portal_uuid=${sourcePortal.portalUuid}';
            """.trimIndent(), "", 0
        )
//        } else {
//        jcefComponent.getBrowser().cefBrowser.executeJavaScript(
//            """
//                  window.location.href = 'http://localhost:8080/traces';
//            """.trimIndent(), "", 0
//        )
//        }

        super.performPopupAction(sourceMark, editor)
    }

    private fun performClassPopup(sourceMark: ClassSourceMark) {
        //todo: get all endpoint keys for current file
    }

    private fun performMethodPopup(sourceMark: MethodSourceMark) {
        val cachedEndpointId = sourceMark.getUserData(ENDPOINT_ID)
        if (cachedEndpointId != null) {
            log.debug("Found cached endpoint id: $cachedEndpointId")
//            updateOverview(cachedEndpointId)
//            updateTraces(cachedEndpointId)
        } else {
            log.debug("Determining endpoint name")
            val endpointName = endpointDetector.determineEndpointName(sourceMark)

            if (endpointName != null) {
                log.debug("Detected endpoint name: $endpointName")

                GlobalScope.launch(vertx.dispatcher()) {
                    log.debug("Determining endpoint id")
                    val endpoint =
                        EndpointTracker.searchExactEndpoint(endpointName, vertx)
                    if (endpoint != null) {
                        sourceMark.putUserData(ENDPOINT_ID, endpoint.id)
                        log.debug("Detected endpoint id: ${endpoint.id}")

//                        updateOverview(endpoint.id)
//                        updateTraces(endpoint.id)
                    } else {
                        log.debug("Could not find endpoint id for: $endpointName")
                    }
                }
            }
        }
    }
}