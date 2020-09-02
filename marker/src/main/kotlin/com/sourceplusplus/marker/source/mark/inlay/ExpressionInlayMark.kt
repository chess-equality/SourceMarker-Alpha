package com.sourceplusplus.marker.source.mark.inlay

import com.sourceplusplus.marker.source.SourceFileMarker
import com.sourceplusplus.marker.source.mark.api.ExpressionSourceMark
import com.sourceplusplus.marker.source.mark.inlay.config.InlayMarkConfiguration
import org.jetbrains.uast.UExpression
import com.sourceplusplus.marker.plugin.SourceMarkerPlugin.configuration as pluginConfiguration

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.2.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
open class ExpressionInlayMark @JvmOverloads constructor(
        override val sourceFileMarker: SourceFileMarker,
        override var psiExpression: UExpression,
        override val configuration: InlayMarkConfiguration = pluginConfiguration.defaultInlayMarkConfiguration.copy()
) : ExpressionSourceMark(sourceFileMarker, psiExpression), InlayMark