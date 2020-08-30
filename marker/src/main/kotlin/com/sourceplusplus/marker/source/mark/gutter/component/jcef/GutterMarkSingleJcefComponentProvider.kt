package com.sourceplusplus.marker.source.mark.gutter.component.jcef

import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEvent
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventCode
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventListener
import com.sourceplusplus.marker.source.mark.gutter.GutterMark
import com.sourceplusplus.marker.source.mark.gutter.event.GutterMarkEventCode

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
class GutterMarkSingleJcefComponentProvider : GutterMarkJcefComponentProvider(), SourceMarkEventListener {

    private val jcefComponent: GutterMarkJcefComponent by lazy {
        GutterMarkJcefComponent(defaultConfiguration.copy())
    }

    /**
     * {@inheritDoc}
     */
    override fun getComponent(gutterMark: GutterMark): GutterMarkJcefComponent {
        gutterMark.addEventListener(this)
        return jcefComponent
    }

    /**
     * {@inheritDoc}
     */
    override fun disposeComponent(gutterMark: GutterMark) {
        //do nothing
    }

    private fun initializeComponent(gutterMark: GutterMark) {
        val jcefComponent = gutterMark.gutterMarkComponent as GutterMarkJcefComponent
        if (jcefComponent.configuration.preloadJcefBrowser) {
            jcefComponent.initialize()
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun handleEvent(event: SourceMarkEvent) {
        when (event.eventCode) {
            GutterMarkEventCode.GUTTER_MARK_VISIBLE -> {
                initializeComponent(event.sourceMark as GutterMark)
            }
            SourceMarkEventCode.MARK_ADDED -> {
                if ((event.sourceMark as GutterMark).configuration.activateOnKeyboardShortcut) {
                    initializeComponent(event.sourceMark)
                }
            }
        }
    }
}