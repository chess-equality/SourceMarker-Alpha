package com.sourceplusplus.protocol.artifact.trace

import kotlinx.datetime.Instant

data class TraceSpanLogEntry(
    val time: Instant,
    val data: String
)