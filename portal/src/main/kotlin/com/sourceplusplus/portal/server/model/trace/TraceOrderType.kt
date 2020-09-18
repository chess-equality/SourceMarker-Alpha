package com.sourceplusplus.portal.server.model.trace

enum class TraceOrderType {
    LATEST_TRACES,
    SLOWEST_TRACES,
    FAILED_TRACES;

    //todo: not need to replace _TRACES?
    val id = name.replace("_TRACES", "").toLowerCase()
    val description = name.toLowerCase().capitalize()
}