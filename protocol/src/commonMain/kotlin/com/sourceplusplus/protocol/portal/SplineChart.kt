package com.sourceplusplus.protocol.portal

data class SplineChart(
    val metricType: MetricType,
    val timeFrame: QueryTimeFrame, //todo: use LocalDuration
    val seriesData: List<SplineSeriesData>
)