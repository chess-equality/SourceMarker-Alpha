package com.sourceplusplus.portal.server.model.trace

enum class TraceTableType(val description: String, val isCentered: Boolean) {
    OPERATION("Operation", false),
    OCCURRED("Occurred", true),
    EXEC("Exec", true),
    EXEC_PCT("Exec (%)", true),
    STATUS("Status", true);
}