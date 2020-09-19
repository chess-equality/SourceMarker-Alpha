package com.sourceplusplus.portal.server.display.tabs.views

import io.vertx.core.json.JsonObject

/**
 * Holds the current view for the Configuration portal tab.
 *
 * @since 0.0.1
 * @author <a href="mailto:bfergerson@apache.org">Brandon Fergerson</a>
 */
class ConfigurationView {

    var artifact: JsonObject? = null

    fun cloneView(view: ConfigurationView) {
        artifact = view.artifact
    }
}
