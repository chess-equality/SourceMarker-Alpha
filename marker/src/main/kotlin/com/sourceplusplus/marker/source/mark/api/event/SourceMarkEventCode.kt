package com.sourceplusplus.marker.source.mark.api.event

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
enum class SourceMarkEventCode(private val code: Int) : IEventCode {
    MARK_ADDED(1000),
    MARK_REMOVED(1001),
    NAME_CHANGED(1002);

    /**
     * {@inheritDoc}
     */
    override fun code(): Int {
        return this.code
    }
}