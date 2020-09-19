package com.sourceplusplus.protocol.artifact.trace

import kotlinx.datetime.Instant

/**
 * todo: description
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
data class TraceSpanLogEntry(
    val time: Instant,
    val data: String
)