package com.sourceplusplus.portal.server.model.trace

import java.time.Instant

data class TraceSpanLogEntry(
    val time: Instant,
    val data: String
)