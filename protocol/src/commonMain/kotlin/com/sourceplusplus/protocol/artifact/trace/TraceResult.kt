package com.sourceplusplus.protocol.artifact.trace

data class TraceResult(
    val appUuid: String?,
    val artifactQualifiedName: String?,
    val artifactSimpleName: String?,
    val orderType: TraceOrderType,
    val start: Long, //todo: Instant
    val stop: Long, //todo: Instant
    val step: String? = null,
    val traces: List<Trace>,
    val total: Int
) {
//    fun mergeWith(result: TraceResult): TraceResult? {
//        var result: TraceResult = result
//        require(appUuid == result.appUuid) { "Mismatching application uuid" }
//        require(artifactQualifiedName == result.artifactQualifiedName) { "Mismatching artifact qualified name" }
//        require(orderType == result.orderType) { "Mismatching order type" }
//        require(step == result.step) { "Mismatching step" }
//        if (start.isBefore(result.start)) {
//            result = result.withStart(start)
//        }
//        if (stop.isAfter(result.stop)) {
//            result = result.withStop(stop)
//        }
//        if (result.artifactSimpleName == null && artifactSimpleName != null) {
//            result = result.withArtifactSimpleName(artifactSimpleName)
//        }
//        val combinedTraces: MutableSet<Trace> = HashSet(traces)
//        combinedTraces.addAll(result.traces)
//        val finalTraces: List<Trace> = ArrayList(combinedTraces)
////        finalTraces.sortedWith(Comparator { t2: Trace, t1: Trace ->
////            if (orderType === TraceOrderType.SLOWEST) {
////                return Integer.compare(t1.duration, t2.duration)
////            } else {
////                return Long.compare(t1.start, t2.start)
////            }
////        })
//        result = result.withTraces(finalTraces)
//        result = result.withTotal(finalTraces.size)
//        return result
//    }

//    fun truncate(amount: Int): TraceResult {
//        return if (traces.size > amount) {
//            this.withTraces(traces.subList(0, amount))
//                .withTotal(traces.size)
//        } else this
//    }
}