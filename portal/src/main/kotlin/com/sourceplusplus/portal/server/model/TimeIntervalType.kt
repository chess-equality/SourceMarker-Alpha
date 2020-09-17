package com.sourceplusplus.portal.server.model

enum class TimeIntervalType(val id: String) {
    FIVE_MINUTES("5_minutes"),
    FIFTEEN_MINUTES("15_minutes"),
    THIRTY_MINUTES("30_minutes"),
    ONE_HOUR("hour"),
    THREE_HOURS("3_hours"); //todo: id = enum name

    val description = id.toUpperCase().replace("_", " ")
}