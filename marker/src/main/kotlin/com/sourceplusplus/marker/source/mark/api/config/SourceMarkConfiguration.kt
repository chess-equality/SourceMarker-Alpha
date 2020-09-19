package com.sourceplusplus.marker.source.mark.api.config

import com.sourceplusplus.marker.source.mark.api.component.api.SourceMarkComponentProvider
import com.sourceplusplus.marker.source.mark.api.filter.ApplySourceMarkFilter

/**
 * todo: description
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
abstract class SourceMarkConfiguration {
    abstract var applySourceMarkFilter: ApplySourceMarkFilter
    abstract var activateOnKeyboardShortcut: Boolean
    abstract var componentProvider: SourceMarkComponentProvider
}