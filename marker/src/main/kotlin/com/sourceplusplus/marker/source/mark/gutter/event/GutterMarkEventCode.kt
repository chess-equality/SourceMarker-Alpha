package com.sourceplusplus.marker.source.mark.gutter.event

import com.sourceplusplus.marker.source.mark.api.event.IEventCode

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
enum class GutterMarkEventCode(private val code: Int) : IEventCode {
    GUTTER_MARK_VISIBLE(2000),
    GUTTER_MARK_HIDDEN(2001);

    /**
     * {@inheritDoc}
     */
    override fun code(): Int {
        return this.code
    }
}