package com.sourceplusplus.protocol.artifact.trace

data class TraceSpanStackQueryResult(
    val traceSpans: List<TraceSpan>,
    val total: Int
)