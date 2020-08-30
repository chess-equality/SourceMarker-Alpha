package com.sourceplusplus.marker.plugin.config

import com.sourceplusplus.marker.SourceFileMarkerProvider
import com.sourceplusplus.marker.source.mark.api.filter.SourceMarkFilter
import com.sourceplusplus.marker.source.mark.gutter.config.GutterMarkConfiguration

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
class SourceMarkerConfiguration {

    var sourceFileMarkerProvider: SourceFileMarkerProvider = object : SourceFileMarkerProvider {}
    var defaultGutterMarkConfiguration: GutterMarkConfiguration = GutterMarkConfiguration() //todo: maybe incorrect location
    var sourceMarkFilter: SourceMarkFilter = SourceMarkFilter.ALL
}