package com.sourceplusplus.marker.source.mark.api.event

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
interface SourceMarkEventListener {

    fun handleEvent(event: SourceMarkEvent)
}