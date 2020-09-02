package com.sourceplusplus.marker.source.mark.api.key

import com.intellij.openapi.util.Key
import com.sourceplusplus.marker.source.mark.gutter.GutterMark
import com.sourceplusplus.marker.source.mark.inlay.InlayMark

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
@Suppress("unused")
data class SourceKey<T>(val name: String) {
    companion object {
        @JvmField
        val GutterMark = Key.create<GutterMark>("sm.GutterMark")

        @JvmField
        val InlayMark = Key.create<InlayMark>("sm.InlayMark")
    }
}