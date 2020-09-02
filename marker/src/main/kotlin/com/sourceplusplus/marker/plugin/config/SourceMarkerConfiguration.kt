package com.sourceplusplus.marker.plugin.config

import com.sourceplusplus.marker.source.SourceFileMarkerProvider
import com.sourceplusplus.marker.source.mark.api.filter.CreateSourceMarkFilter
import com.sourceplusplus.marker.source.mark.gutter.config.GutterMarkConfiguration
import com.sourceplusplus.marker.source.mark.inlay.config.InlayMarkConfiguration

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
class SourceMarkerConfiguration {
    var createSourceMarkFilter: CreateSourceMarkFilter = CreateSourceMarkFilter.ALL
    var sourceFileMarkerProvider: SourceFileMarkerProvider = object : SourceFileMarkerProvider {}
    var defaultGutterMarkConfiguration: GutterMarkConfiguration = GutterMarkConfiguration() //todo: maybe incorrect location
    var defaultInlayMarkConfiguration: InlayMarkConfiguration = InlayMarkConfiguration() //todo: maybe incorrect location
}