package com.sourceplusplus.marker.source.mark.gutter

import com.sourceplusplus.marker.source.SourceFileMarker
import com.sourceplusplus.marker.source.mark.api.MethodSourceMark
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEvent
import com.sourceplusplus.marker.source.mark.gutter.config.GutterMarkConfiguration
import com.sourceplusplus.marker.source.mark.gutter.event.GutterMarkEventCode
import org.jetbrains.uast.UMethod
import java.util.concurrent.atomic.AtomicBoolean
import com.sourceplusplus.marker.plugin.SourceMarkerPlugin.configuration as pluginConfiguration

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
open class MethodGutterMark(
        override val sourceFileMarker: SourceFileMarker,
        override var psiMethod: UMethod
) : MethodSourceMark(sourceFileMarker, psiMethod), GutterMark {

    override val configuration: GutterMarkConfiguration = pluginConfiguration.defaultGutterMarkConfiguration.copy()
    private var visible: AtomicBoolean = AtomicBoolean()

    /**
     * {@inheritDoc}
     */
    override fun isVisible(): Boolean {
        return visible.get()
    }

    /**
     * {@inheritDoc}
     */
    override fun setVisible(visible: Boolean) {
        val previousVisibility = this.visible.getAndSet(visible)
        if (visible && !previousVisibility) {
            triggerEvent(SourceMarkEvent(this, GutterMarkEventCode.GUTTER_MARK_VISIBLE))
        } else if (!visible && previousVisibility) {
            triggerEvent(SourceMarkEvent(this, GutterMarkEventCode.GUTTER_MARK_HIDDEN))
        }
    }
}