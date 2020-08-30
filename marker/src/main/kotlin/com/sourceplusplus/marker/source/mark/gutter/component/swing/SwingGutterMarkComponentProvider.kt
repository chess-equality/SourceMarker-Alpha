package com.sourceplusplus.marker.source.mark.gutter.component.swing

import com.sourceplusplus.marker.source.mark.gutter.GutterMark
import com.sourceplusplus.marker.source.mark.gutter.component.api.GutterMarkComponent
import com.sourceplusplus.marker.source.mark.gutter.component.api.GutterMarkComponentProvider
import com.sourceplusplus.marker.source.mark.gutter.component.api.config.GutterMarkComponentConfiguration
import javax.swing.JComponent

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
abstract class SwingGutterMarkComponentProvider : GutterMarkComponentProvider() {

    override val defaultConfiguration = GutterMarkComponentConfiguration()

    abstract fun makeSwingComponent(gutterMark: GutterMark): JComponent

    /**
     * {@inheritDoc}
     */
    override fun getComponent(gutterMark: GutterMark): GutterMarkComponent {
        val component = makeSwingComponent(gutterMark)
        return object : GutterMarkComponent {
            override val configuration = defaultConfiguration.copy()

            override fun getComponent(): JComponent {
                return component
            }

            override fun dispose() {
                //do nothing
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun disposeComponent(gutterMark: GutterMark) {
        //do nothing
    }
}