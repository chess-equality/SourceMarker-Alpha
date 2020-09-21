package com.sourceplusplus.portal.frontend.tabs

import com.sourceplusplus.portal.frontend.SourcePortal
import com.sourceplusplus.protocol.ProtocolAddress.Global.Companion.OpenedPortal
import com.sourceplusplus.protocol.portal.PageType
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject

/**
 * Contains common portal tab functionality.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
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
