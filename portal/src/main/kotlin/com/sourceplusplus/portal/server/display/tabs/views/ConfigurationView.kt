package com.sourceplusplus.portal.server.display.tabs.views

import io.vertx.core.json.JsonObject

/**
 * Holds the current view for the Configuration portal tab.
 *
 * @version 0.3.2
 * @since 0.2.0
 * @author <a href="mailto:brandon@srcpl.us">Brandon Fergerson</a>
 */
class ConfigurationView {

    var artifact: JsonObject? = null

    fun cloneView(view: ConfigurationView) {
        artifact = view.artifact
    }
}
