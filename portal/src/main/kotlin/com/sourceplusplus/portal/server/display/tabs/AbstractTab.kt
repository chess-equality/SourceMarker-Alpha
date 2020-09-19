package com.sourceplusplus.portal.server.display.tabs

import com.sourceplusplus.portal.server.display.PortalViewTracker
import com.sourceplusplus.portal.server.display.PortalTab
import com.sourceplusplus.portal.server.display.SourcePortal
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject

/**
 * Contains common portal tab functionality.
 *
 * @version 0.3.2
 * @since 0.2.0
 * @author <a href="mailto:brandon@srcpl.us">Brandon Fergerson</a>
 */
abstract class AbstractTab(val thisTab: PortalTab) : AbstractVerticle() {

    override fun start() {
        vertx.eventBus().consumer<JsonObject>(PortalViewTracker.OPENED_PORTAL) {
            val portal = SourcePortal.getPortal(JsonObject.mapFrom(it.body()).getString("portal_uuid"))!!
            if (portal.portalUI.currentTab == thisTab) {
                updateUI(portal)
            }
        }
    }

    abstract fun updateUI(portal: SourcePortal)
}
