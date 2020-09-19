package com.sourceplusplus.protocol.artifact.trace

data class InnerTraceStackInfo(
    val innerLevel: Int,
    val traceStack: String //todo: was JsonArray
)