package com.sourceplusplus.marker.source.mark.inlay

import com.sourceplusplus.marker.source.mark.api.SourceMark
import com.sourceplusplus.marker.source.mark.inlay.config.InlayMarkConfiguration
import org.slf4j.LoggerFactory

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.2.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
interface InlayMark : SourceMark {

    companion object {
        private val log = LoggerFactory.getLogger(InlayMark::class.java)
    }

    override val type: SourceMark.Type
        get() = SourceMark.Type.INLAY
    override val configuration: InlayMarkConfiguration
}