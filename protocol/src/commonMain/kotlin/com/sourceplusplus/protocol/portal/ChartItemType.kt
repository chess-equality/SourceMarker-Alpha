package com.sourceplusplus.protocol.portal

/**
 * todo: description
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
enum class ChartItemType(val type: String, val abbr: String, val id: String, val description: String, val label: String) {
    AVG_THROUGHPUT("average", "AVG", "throughput", "Throughput", "THROUGHPUT"),
    AVG_RESPONSE_TIME("average", "AVG", "responsetime", "ResponseTime", "RESP TIME"),
    AVG_SLA("average", "AVG", "servicelevelagreement", "SLA", "SLA")
}