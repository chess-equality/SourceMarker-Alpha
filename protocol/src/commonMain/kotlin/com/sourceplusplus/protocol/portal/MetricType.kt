package com.sourceplusplus.protocol.portal

enum class MetricType {
    Throughput_Average,
    ResponseTime_Average,
    ServiceLevelAgreement_Average,
    ResponseTime_99Percentile,
    ResponseTime_95Percentile,
    ResponseTime_90Percentile,
    ResponseTime_75Percentile,
    ResponseTime_50Percentile;

    companion object {
        //todo: remove
        fun realValueOf(name: String): MetricType {
            return (values().find { it.name == name }
                ?: when (name) {
                    "endpoint_cpm" -> Throughput_Average
                    "endpoint_avg" -> ResponseTime_Average
                    "endpoint_sla" -> ServiceLevelAgreement_Average
                    "endpoint_percentile" -> ResponseTime_99Percentile
                    else -> throw UnsupportedOperationException(name)
                })
        }
    }
}