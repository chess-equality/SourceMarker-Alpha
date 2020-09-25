package com.sourceplusplus.monitor.skywalking.model

import com.sourceplusplus.protocol.artifact.trace.TraceOrderType

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class GetEndpointTraces(
    val appUuid: String,
    val artifactQualifiedName: String,
    val endpointId: String,
    val zonedDuration: ZonedDuration,
    val orderType: TraceOrderType = TraceOrderType.LATEST_TRACES,
    val pageNumber: Int = 1,
    val pageSize: Int = 10
)
