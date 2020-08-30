package com.sourceplusplus.marker.source.mark.gutter.config

import com.sourceplusplus.marker.source.mark.gutter.component.api.GutterMarkComponentProvider
import com.sourceplusplus.marker.source.mark.gutter.component.jcef.GutterMarkJcefComponentProvider
import javax.swing.Icon

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
data class GutterMarkConfiguration(
        var icon: Icon? = null,
        var activateOnMouseHover: Boolean = true,
        var activateOnMouseClick: Boolean = false,
        var activateOnKeyboardShortcut: Boolean = false,
        var componentProvider: GutterMarkComponentProvider = GutterMarkJcefComponentProvider()
)