package com.sourceplusplus.portal.server.model.trace

enum class TraceType {
    LATEST,
    SLOWEST,
    FAILED;

    val id = name.toLowerCase()
    val description = name.toLowerCase().capitalize()
}