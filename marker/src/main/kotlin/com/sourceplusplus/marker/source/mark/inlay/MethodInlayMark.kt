package com.sourceplusplus.marker.source.mark.inlay

import com.sourceplusplus.marker.source.SourceFileMarker
import com.sourceplusplus.marker.source.mark.api.MethodSourceMark
import com.sourceplusplus.marker.source.mark.inlay.config.InlayMarkConfiguration
import org.jetbrains.uast.UMethod
import com.sourceplusplus.marker.plugin.SourceMarkerPlugin.configuration as pluginConfiguration

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.2.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
open class MethodInlayMark @JvmOverloads constructor(
        override val sourceFileMarker: SourceFileMarker,
        override var psiMethod: UMethod,
        override val configuration: InlayMarkConfiguration = pluginConfiguration.defaultInlayMarkConfiguration.copy()
) : MethodSourceMark(sourceFileMarker, psiMethod), InlayMark