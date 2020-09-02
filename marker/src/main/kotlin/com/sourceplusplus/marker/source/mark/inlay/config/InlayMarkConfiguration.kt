package com.sourceplusplus.marker.source.mark.inlay.config

import com.sourceplusplus.marker.source.mark.api.component.api.SourceMarkComponentProvider
import com.sourceplusplus.marker.source.mark.api.component.jcef.SourceMarkJcefComponentProvider
import com.sourceplusplus.marker.source.mark.api.config.SourceMarkConfiguration
import com.sourceplusplus.marker.source.mark.api.filter.ApplySourceMarkFilter

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.2.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
data class InlayMarkConfiguration(
        override var applySourceMarkFilter: ApplySourceMarkFilter = ApplySourceMarkFilter.NONE,
        var virtualText: InlayMarkVirtualText? = null,
        var activateOnMouseClick: Boolean = true,
        override var activateOnKeyboardShortcut: Boolean = false, //todo: remove
        override var componentProvider: SourceMarkComponentProvider = SourceMarkJcefComponentProvider()
) : SourceMarkConfiguration()