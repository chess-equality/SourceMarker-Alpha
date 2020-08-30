package com.sourceplusplus.marker.source.mark.gutter.component.api

import com.sourceplusplus.marker.source.mark.gutter.component.api.config.GutterMarkComponentConfiguration
import javax.swing.JComponent

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
interface GutterMarkComponent {

    val configuration: GutterMarkComponentConfiguration

    fun getComponent(): JComponent
    fun dispose()
}