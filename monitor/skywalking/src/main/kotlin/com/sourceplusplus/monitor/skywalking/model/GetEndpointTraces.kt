package com.sourceplusplus.monitor.skywalking.model

import monitor.skywalking.protocol.type.Pagination
import monitor.skywalking.protocol.type.QueryOrder
import monitor.skywalking.protocol.type.TraceState

/**
 * todo: description
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class GetEndpointTraces(
    val appUuid: String,
    val artifactQualifiedName: String,
    val endpointId: String,
    val zonedDuration: ZonedDuration,
    val queryOrder: QueryOrder = QueryOrder.BY_START_TIME,
    val traceState: TraceState = TraceState.ALL,
    val paging: Pagination = Pagination(pageSize = 10)
)