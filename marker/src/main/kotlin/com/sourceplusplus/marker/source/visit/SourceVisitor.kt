package com.sourceplusplus.marker.source.visit

/**
 * Visits source files for mark-able elements.
 * <p>
 * Currently supports: methods
 *
 * @version 0.2.2
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
interface SourceVisitor {
    val visitedMethods: List<String>
}