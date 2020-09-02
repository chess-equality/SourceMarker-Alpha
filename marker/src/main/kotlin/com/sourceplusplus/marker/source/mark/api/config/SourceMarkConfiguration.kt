package com.sourceplusplus.marker.source.mark.api.config

import com.sourceplusplus.marker.source.mark.api.component.api.SourceMarkComponentProvider
import com.sourceplusplus.marker.source.mark.api.filter.ApplySourceMarkFilter

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.2.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
abstract class SourceMarkConfiguration {
    abstract var applySourceMarkFilter: ApplySourceMarkFilter
    abstract var activateOnKeyboardShortcut: Boolean
    abstract var componentProvider: SourceMarkComponentProvider
}