package com.sourceplusplus.sourcemarker.listeners

import com.intellij.openapi.application.ApplicationManager
import com.sourceplusplus.marker.plugin.SourceMarkerPlugin
import com.sourceplusplus.marker.source.mark.api.SourceMark
import com.sourceplusplus.portal.server.display.SourcePortal
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.ClosePortal
import io.vertx.kotlin.coroutines.CoroutineVerticle

class PortalEventListener : CoroutineVerticle() {

    override suspend fun start() {
        vertx.eventBus().consumer<SourcePortal>(ClosePortal) {
            val sourceMark =
                SourceMarkerPlugin.getSourceMark(it.body().portalUI.viewingPortalArtifact, SourceMark.Type.GUTTER)
            if (sourceMark != null) {
                ApplicationManager.getApplication().invokeLater(sourceMark::closePopup)
            }
        }
    }
}