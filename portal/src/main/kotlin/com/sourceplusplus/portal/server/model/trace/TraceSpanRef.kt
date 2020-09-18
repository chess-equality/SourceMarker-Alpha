package com.sourceplusplus.portal.server.model.trace

data class TraceSpanRef(
    val traceId: String,
    val parentSegmentId: String,
    val parentSpanId: Long,
    val type: String
)