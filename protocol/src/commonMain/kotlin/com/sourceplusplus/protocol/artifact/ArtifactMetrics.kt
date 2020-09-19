package com.sourceplusplus.protocol.artifact

import com.sourceplusplus.protocol.portal.MetricType

data class ArtifactMetrics(
    val metricType: MetricType,
    val values: List<Int>
)