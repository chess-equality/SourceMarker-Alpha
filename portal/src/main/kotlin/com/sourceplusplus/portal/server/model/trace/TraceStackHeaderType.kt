package com.sourceplusplus.portal.server.model.trace

enum class TraceStackHeaderType(val id: String, val icon: String) {
    TRACE_ID("trace_id", "crosshairs"),
    TIME_OCCURRED("time_occurred", "clock outline")
}