package com.sourceplusplus.marker.source.mark.api.filter

import com.sourceplusplus.marker.source.mark.api.SourceMark
import java.util.function.Predicate

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
//todo: generic
interface ApplySourceMarkFilter : Predicate<SourceMark> {
    companion object {
        @JvmStatic
        val ALL: ApplySourceMarkFilter = object : ApplySourceMarkFilter {
            override fun test(sourceMark: SourceMark): Boolean = true
        }

        @JvmStatic
        val NONE: ApplySourceMarkFilter = object : ApplySourceMarkFilter {
            override fun test(sourceMark: SourceMark): Boolean = false
        }
    }
}