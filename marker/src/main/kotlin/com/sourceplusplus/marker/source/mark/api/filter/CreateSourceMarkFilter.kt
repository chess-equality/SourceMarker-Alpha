package com.sourceplusplus.marker.source.mark.api.filter

import java.util.function.Predicate

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.2.2
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
fun interface CreateSourceMarkFilter : Predicate<String> {
    companion object {
        @JvmStatic
        val ALL: CreateSourceMarkFilter = CreateSourceMarkFilter { true }

        @JvmStatic
        val NONE: CreateSourceMarkFilter = CreateSourceMarkFilter { false }
    }
}