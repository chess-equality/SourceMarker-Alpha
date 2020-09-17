package com.sourceplusplus.portal.server.model

enum class ChartItemType(val type: String, val abbr: String, val id: String, val description: String, val label: String) {
    AVG_THROUGHPUT("average", "AVG", "throughput", "Throughput", "THROUGHPUT"),
    AVG_RESPONSE_TIME("average", "AVG", "responsetime", "ResponseTime", "RESP TIME"),
    AVG_SLA("average", "AVG", "servicelevelagreement", "SLA", "SLA")
}