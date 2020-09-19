package com.sourceplusplus.protocol.artifact

import com.sourceplusplus.protocol.portal.QueryTimeFrame
import kotlinx.datetime.Instant

data class ArtifactMetricResult(
    val appUuid: String?,
    val artifactQualifiedName: String,
    val timeFrame: QueryTimeFrame,
    val start: Instant,
    val stop: Instant,
    val step: String,
    val artifactMetrics: List<ArtifactMetrics>
)