package com.sourceplusplus.marker.source.mark.inlay.event

import com.sourceplusplus.marker.source.mark.api.event.IEventCode

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.2.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
enum class InlayMarkEventCode(private val code: Int) : IEventCode {
    VIRTUAL_TEXT_UPDATED(3000);

    /**
     * {@inheritDoc}
     */
    override fun code(): Int {
        return this.code
    }
}