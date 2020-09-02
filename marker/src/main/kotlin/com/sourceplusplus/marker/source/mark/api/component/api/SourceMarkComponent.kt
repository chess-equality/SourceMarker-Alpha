package com.sourceplusplus.marker.source.mark.api.component.api

import com.sourceplusplus.marker.source.mark.api.component.api.config.SourceMarkComponentConfiguration
import javax.swing.JComponent

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
interface SourceMarkComponent {

    val configuration: SourceMarkComponentConfiguration

    fun getComponent(): JComponent
    fun dispose()
}