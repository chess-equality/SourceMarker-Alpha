package com.sourceplusplus.marker.source.mark.gutter.config

import com.intellij.openapi.util.IconLoader

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class GutterMarkIcon {
    companion object {
        @JvmField
        val rabbitFast = IconLoader.getIcon("/icons/fontawesome/rabbit-fast.svg", GutterMarkIcon::class.java)

        @JvmField
        val globe = IconLoader.getIcon("/icons/fontawesome/globe.svg", GutterMarkIcon::class.java)
    }
}
