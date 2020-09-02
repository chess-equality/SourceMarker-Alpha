package com.sourceplusplus.marker.source.mark.gutter.config

import com.intellij.openapi.util.IconLoader

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
class GutterMarkIcon {
    companion object {
        @JvmField
        val rabbitFast = IconLoader.getIcon("/icons/fontawesome/rabbit-fast.svg", GutterMarkIcon::class.java)

        @JvmField
        val globe = IconLoader.getIcon("/icons/fontawesome/globe.svg", GutterMarkIcon::class.java)
    }
}