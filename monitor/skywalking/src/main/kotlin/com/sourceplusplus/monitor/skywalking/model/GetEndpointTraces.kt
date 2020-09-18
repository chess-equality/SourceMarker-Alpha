package com.sourceplusplus.monitor.skywalking.model

import monitor.skywalking.protocol.type.Pagination
import monitor.skywalking.protocol.type.QueryOrder
import monitor.skywalking.protocol.type.TraceState

class GetEndpointTraces(
    val endpointId: String,
    val zonedDuration: ZonedDuration,
    val queryOrder: QueryOrder = QueryOrder.BY_START_TIME,
    val traceState: TraceState = TraceState.ALL,
    val paging: Pagination = Pagination(pageSize = 10)
)