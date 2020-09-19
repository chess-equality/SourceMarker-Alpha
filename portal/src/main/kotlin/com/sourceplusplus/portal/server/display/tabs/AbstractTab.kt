package com.sourceplusplus.portal.server.display.tabs

import com.sourceplusplus.portal.server.display.SourcePortal
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.OpenedPortal
import com.sourceplusplus.protocol.portal.PageType
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject

/**
 * Contains common portal tab functionality.
 *
 * @version 0.3.2
 * @since 0.2.0
 * @author <a href="mailto:brandon@srcpl.us">Brandon Fergerson</a>
 */
abstract class AbstractTab(val thisTab: PageType) : AbstractVerticle() {

    override fun start() {
        vertx.eventBus().consumer<JsonObject>(OpenedPortal) {
            val portal = SourcePortal.getPortal(JsonObject.mapFrom(it.body()).getString("portal_uuid"))!!
            if (portal.currentTab == thisTab) {
                updateUI(portal)
            }
        }
    }

    abstract fun updateUI(portal: SourcePortal)
}
