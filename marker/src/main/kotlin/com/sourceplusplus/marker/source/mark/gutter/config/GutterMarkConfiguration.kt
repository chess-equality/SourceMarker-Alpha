package com.sourceplusplus.marker.source.mark.gutter.config

import com.sourceplusplus.marker.source.mark.api.component.api.SourceMarkComponentProvider
import com.sourceplusplus.marker.source.mark.api.component.jcef.SourceMarkJcefComponentProvider
import com.sourceplusplus.marker.source.mark.api.config.SourceMarkConfiguration
import com.sourceplusplus.marker.source.mark.api.filter.ApplySourceMarkFilter
import javax.swing.Icon

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
data class GutterMarkConfiguration(
        override var applySourceMarkFilter: ApplySourceMarkFilter = ApplySourceMarkFilter.ALL,
        var icon: Icon? = null,
        var activateOnMouseHover: Boolean = true,
        var activateOnMouseClick: Boolean = false,
        override var activateOnKeyboardShortcut: Boolean = false,
        override var componentProvider: SourceMarkComponentProvider = SourceMarkJcefComponentProvider()
) : SourceMarkConfiguration()