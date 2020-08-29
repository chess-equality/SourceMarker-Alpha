package com.sourceplusplus.marker.source.mark.api.filter

import com.sourceplusplus.marker.source.mark.api.SourceMark
import java.util.function.Predicate

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
abstract class SourceMarkFilter : Predicate<SourceMark> {

    companion object {
        @JvmStatic
        val ALL: SourceMarkFilter = AllFilter()
    }

    private class AllFilter : SourceMarkFilter() {
        override fun test(sourceMark: SourceMark) = true
    }
}