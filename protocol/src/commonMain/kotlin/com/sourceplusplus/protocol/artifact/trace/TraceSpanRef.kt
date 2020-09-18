package com.sourceplusplus.protocol.artifact.trace

data class TraceSpanRef(
    val traceId: String,
    val parentSegmentId: String,
    val parentSpanId: Int,
    val type: String
)