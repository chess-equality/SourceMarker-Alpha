package com.sourceplusplus.marker.source.mark.gutter.component.jcef.config

import com.sourceplusplus.marker.source.mark.gutter.component.api.config.GutterMarkComponentConfiguration
import java.awt.Dimension

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
class GutterMarkJcefComponentConfiguration : GutterMarkComponentConfiguration() {

    var preloadJcefBrowser: Boolean = true
    var initialUrl: String = "about:blank"
    var initialHtml: String? = null
    var componentWidth: Int = 400
    var componentHeight: Int = 300
    var autoDisposeBrowser: Boolean = true

    fun setComponentSize(size: Dimension) {
        componentWidth = size.width
        componentHeight = size.height
    }

    /**
     * {@inheritDoc}
     */
    override fun copy(): GutterMarkJcefComponentConfiguration {
        val copy = GutterMarkJcefComponentConfiguration()
        copy.useHeavyPopup = useHeavyPopup
        copy.hideOnMouseMotion = hideOnMouseMotion
        copy.hideOnScroll = hideOnScroll
        copy.showAboveClass = showAboveClass
        copy.showAboveMethod = showAboveMethod
        copy.componentSizeEvaluator = componentSizeEvaluator

        copy.preloadJcefBrowser = preloadJcefBrowser
        copy.initialUrl = initialUrl
        copy.initialHtml = initialHtml
        copy.componentWidth = componentWidth
        copy.componentHeight = componentHeight
        copy.autoDisposeBrowser = autoDisposeBrowser
        return copy
    }
}
