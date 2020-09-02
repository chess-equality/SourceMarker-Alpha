package com.sourceplusplus.marker.source.mark.gutter

import com.sourceplusplus.marker.source.mark.api.SourceMark
import com.sourceplusplus.marker.source.mark.gutter.config.GutterMarkConfiguration
import org.slf4j.LoggerFactory

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
interface GutterMark : SourceMark {

    companion object {
        private val log = LoggerFactory.getLogger(GutterMark::class.java)
    }

    override val type: SourceMark.Type
        get() = SourceMark.Type.GUTTER
    override val configuration: GutterMarkConfiguration

    fun isVisible(): Boolean
    fun setVisible(visible: Boolean)
}