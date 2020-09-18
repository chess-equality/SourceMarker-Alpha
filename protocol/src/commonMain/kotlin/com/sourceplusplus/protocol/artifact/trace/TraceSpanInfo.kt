package com.sourceplusplus.protocol.artifact.trace

//todo: remove ?s
data class TraceSpanInfo(
    val span: TraceSpan,
    val timeTook: String,
    val appUuid: String,
    val rootArtifactQualifiedName: String,
    val operationName: String,
    val totalTracePercent: Double
)