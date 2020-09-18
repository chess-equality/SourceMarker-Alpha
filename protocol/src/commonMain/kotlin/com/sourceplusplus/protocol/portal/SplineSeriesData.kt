package com.sourceplusplus.protocol.portal

data class SplineSeriesData(
    val seriesIndex: Int,
    val times: List<Long>, //todo: Instant
    val values: DoubleArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SplineSeriesData) return false

        if (seriesIndex != other.seriesIndex) return false
        if (times != other.times) return false
        if (!values.contentEquals(other.values)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = seriesIndex
        result = 31 * result + times.hashCode()
        result = 31 * result + values.contentHashCode()
        return result
    }
}