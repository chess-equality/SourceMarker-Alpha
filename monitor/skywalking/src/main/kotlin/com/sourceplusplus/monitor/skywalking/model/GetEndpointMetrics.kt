package com.sourceplusplus.monitor.skywalking.model

data class GetEndpointMetrics(
    val metricIds: List<String>,
    val endpointId: String,
    val localDuration: LocalDuration
)