package com.sourceplusplus.marker.source.mark.api.event

import com.sourceplusplus.marker.source.mark.api.SourceMark

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
open class SourceMarkEvent(
        val sourceMark: SourceMark,
        val eventCode: IEventCode,
        vararg val params: Any
) {

    /**
     * {@inheritDoc}
     */
    override fun toString(): String {
        return if (params.isEmpty()) {
            "Event: $eventCode - Source: $sourceMark"
        } else {
            "Event: $eventCode - Source: $sourceMark - Params: $params"
        }
    }
}
