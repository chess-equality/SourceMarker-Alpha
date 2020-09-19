package com.sourceplusplus.marker.source.mark.api.event

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
fun interface SourceMarkEventListener {
    fun handleEvent(event: SourceMarkEvent)
}