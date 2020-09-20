package com.sourceplusplus.protocol.artifact.trace

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
data class TraceResult(
    val appUuid: String,
    val artifactQualifiedName: String,
    val artifactSimpleName: String? = null,
    val orderType: TraceOrderType,
    val start: Long, //todo: Instant
    val stop: Long, //todo: Instant
    val step: String? = null,
    val traces: List<Trace>,
    val total: Int
) {
    fun mergeWith(traceResult: TraceResult): TraceResult {
        var result: TraceResult = traceResult
        require(appUuid == result.appUuid) { "Mismatching application uuid" }
        require(artifactQualifiedName == result.artifactQualifiedName) { "Mismatching artifact qualified name" }
        require(orderType == result.orderType) { "Mismatching order type" }
        require(step == result.step) { "Mismatching step" }
        if (start < result.start) {
            result = result.copy(start = start)
        }
        if (stop > result.stop) {
            result = result.copy(stop = stop)
        }
        if (result.artifactSimpleName == null && artifactSimpleName != null) {
            result = result.copy(artifactSimpleName = artifactSimpleName)
        }
        val combinedTraces: MutableSet<Trace> = HashSet(traces)
        combinedTraces.addAll(result.traces)
        val finalTraces = ArrayList(combinedTraces).sortedWith(Comparator { t2: Trace, t1: Trace ->
            if (orderType == TraceOrderType.SLOWEST_TRACES) {
                return@Comparator t1.duration.compareTo(t2.duration)
            } else {
                return@Comparator t1.start.compareTo(t2.start)
            }
        })
        return result.copy(traces = finalTraces, total = finalTraces.size)
    }

    fun truncate(amount: Int): TraceResult {
        return if (traces.size > amount) {
            copy(traces = traces.subList(0, amount), total = traces.size)
        } else this
    }
}
