package com.sourceplusplus.portal.server.model

enum class TimeIntervalType(val id: String, val description: String) {
    FIVE_MINUTES("5_minutes", "5 MINUTES"),
    FIFTEEN_MINUTES("15_minutes", "15 MINUTES"),
    THIRTY_MINUTES("30_minutes", "30 MINUTES"),
    ONE_HOUR("hour", "HOUR"),
    THREE_HOURS("3_hours", "3 HOURS");
}