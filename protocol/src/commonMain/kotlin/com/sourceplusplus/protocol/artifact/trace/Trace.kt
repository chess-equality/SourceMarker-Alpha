package com.sourceplusplus.protocol.artifact.trace

//todo: remove ?s
data class Trace(
    val key: String? = null,
    val operationNames: List<String>,
    val duration: Int,
    val start: Long,
    val error: Boolean,
    val traceIds: List<String>,
    val prettyDuration: String,
    val partial: Boolean? = null
)