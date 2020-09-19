package com.sourceplusplus.portal.server.display

import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject

/**
 * Used to track the current viewable state of the Source++ Portal.
 *
 * Recognizes and produces messages for the following events:
 *  - user hovered over S++ icon
 *  - user opened/closed portal
 *
 * @version 0.3.2
 * @since 0.1.0
 * @author <a href="mailto:brandon@srcpl.us">Brandon Fergerson</a>
 */
class PortalViewTracker : AbstractVerticle() {

    companion object {
        val KEEP_ALIVE_PORTAL = "KeepAlivePortal"
        val UPDATE_PORTAL_ARTIFACT = "UpdatePortalArtifact"
        val CAN_OPEN_PORTAL = "CanOpenPortal"
        val OPENED_PORTAL = "OpenedPortal"
        val CLOSED_PORTAL = "ClosedPortal"
        val CHANGED_PORTAL_ARTIFACT = "ChangedPortalArtifact"
        val CLICKED_VIEW_AS_EXTERNAL_PORTAL = "ClickedViewAsExternalPortal"
    }

    override fun start() {
        //get portal from cache to ensure it remains active
        vertx.eventBus().consumer<JsonObject>(KEEP_ALIVE_PORTAL) { messageHandler ->
            val portalUuid = JsonObject.mapFrom(messageHandler.body()).getString("portal_uuid")
            val portal = SourcePortal.getPortal(portalUuid)
            if (portal != null) {
                SourcePortal.ensurePortalActive(portal)
                messageHandler.reply(200)
            } else {
//                log.warn("Failed to find portal. Portal UUID: {}", portalUuid)
                messageHandler.fail(404, "Portal not found")
            }
        }

        //user wants to open portal
        vertx.eventBus().consumer<Void>(CAN_OPEN_PORTAL) { messageHandler ->
            messageHandler.reply(true)
        }

        //user wants a new external portal
        vertx.eventBus().consumer<JsonObject>(CLICKED_VIEW_AS_EXTERNAL_PORTAL) { messageHandler ->
            val portal = SourcePortal.getPortal(JsonObject.mapFrom(messageHandler.body()).getString("portal_uuid"))!!
            messageHandler.reply(JsonObject().put("portal_uuid", portal.createExternalPortal().portalUuid))
        }

        //user opened portal
        vertx.eventBus().consumer<Any>(OPENED_PORTAL) {
            println("here")
//            if (it.body() is SourceArtifact) {
//                val artifact = it.body() as SourceArtifact
////                log.info("Showing Source++ Portal for artifact: {}", getShortQualifiedFunctionName(artifact.artifactQualifiedName()))
//                //todo: reset ui if artifact different than last artifact
//            }
        }

        //user closed portal
        vertx.eventBus().consumer<Any>(CLOSED_PORTAL) {
            println("here")
//            if (it.body() is SourceArtifact) {
//                val artifact = it.body() as SourceArtifact
////                log.info("Hiding Source++ Portal for artifact: {}", getShortQualifiedFunctionName(artifact.artifactQualifiedName()))
//            }
        }

        vertx.eventBus().consumer<JsonObject>(UPDATE_PORTAL_ARTIFACT) {
            val request = JsonObject.mapFrom(it.body())
            val portalUuid = request.getString("portal_uuid")
            val artifactQualifiedName = request.getString("artifact_qualified_name")

            val portal = SourcePortal.getPortal(portalUuid)!!
            if (artifactQualifiedName != portal.portalUI.viewingPortalArtifact) {
                portal.portalUI.viewingPortalArtifact = artifactQualifiedName
                vertx.eventBus().publish(
                    CHANGED_PORTAL_ARTIFACT,
                    JsonObject().put("portal_uuid", portalUuid)
                        .put("artifact_qualified_name", artifactQualifiedName)
                )
            }
        }
//        log.info("{} started", getClass().getSimpleName())
    }
}
