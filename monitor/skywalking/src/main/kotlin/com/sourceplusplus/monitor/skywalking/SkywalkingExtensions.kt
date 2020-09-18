package com.sourceplusplus.monitor.skywalking

import com.sourceplusplus.protocol.artifact.trace.*
import kotlinx.datetime.Instant
import monitor.skywalking.protocol.metrics.GetLinearIntValuesQuery
import monitor.skywalking.protocol.trace.QueryBasicTracesQuery
import monitor.skywalking.protocol.trace.QueryTraceQuery
import java.math.BigDecimal

fun GetLinearIntValuesQuery.Result.toDoubleArray(): DoubleArray {
    return values.map { (it.value as BigDecimal).toDouble() }.toDoubleArray()
}

fun QueryBasicTracesQuery.Trace.toProtocol(): Trace {
    return Trace(
        segmentId = segmentId,
        operationNames = endpointNames,
        duration = duration,
        start = start.toLong(),
        error = isError,
        traceIds = traceIds,
        prettyDuration = "10s" //todo: generated from duration
    )
}

fun QueryTraceQuery.Result.toProtocol(): TraceSpanStack {
    return TraceSpanStack(spans.map {
        TraceSpanInfo(
            span = it.toProtocol(),
            appUuid = "todo1",
            operationName = "todo2",
            rootArtifactQualifiedName = "todo3",
            timeTook = "todo4",
            totalTracePercent = 10.0
        )
    })
}

fun QueryTraceQuery.Log.toProtocol(): TraceSpanLogEntry {
    return TraceSpanLogEntry(
        time = Instant.fromEpochMilliseconds(time as Long),
        data = "todo" //todo:
    )
}

fun QueryTraceQuery.Ref.toProtocol(): TraceSpanRef {
    return TraceSpanRef(
        traceId = traceId,
        parentSegmentId = parentSegmentId,
        parentSpanId = parentSpanId,
        type = type.name
    )
}

fun QueryTraceQuery.Span.toProtocol(): TraceSpan {
    return TraceSpan(
        traceId = traceId,
        segmentId = segmentId,
        spanId = spanId,
        parentSpanId = parentSpanId,
        refs = refs.map { it.toProtocol() },
        serviceCode = serviceCode,
        startTime = startTime as Long,
        endTime = endTime as Long,
        endpointName = endpointName,
        type = type,
        peer = peer,
        component = component,
        error = isError,
        layer = layer,
        tags = tags.map { it.key to it.value!! }.toMap(),
        logs = logs.map { it.toProtocol() }
    )
}