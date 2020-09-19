package com.sourceplusplus.monitor.skywalking

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.toDeferred
import com.sourceplusplus.monitor.skywalking.model.GetEndpointMetrics
import com.sourceplusplus.monitor.skywalking.model.GetEndpointTraces
import com.sourceplusplus.protocol.artifact.trace.TraceResult
import io.vertx.core.Vertx
import monitor.skywalking.protocol.metadata.GetAllServicesQuery
import monitor.skywalking.protocol.metadata.GetServiceInstancesQuery
import monitor.skywalking.protocol.metadata.SearchEndpointQuery
import monitor.skywalking.protocol.metrics.GetLinearIntValuesQuery
import monitor.skywalking.protocol.trace.QueryBasicTracesQuery
import monitor.skywalking.protocol.trace.QueryTraceQuery
import monitor.skywalking.protocol.type.*
import org.slf4j.LoggerFactory
import java.time.ZoneOffset.ofHours
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SkywalkingClient(
    val vertx: Vertx,
    private val apolloClient: ApolloClient,
    private val timezoneOffset: Int = 0
) {

    companion object {
        private val log = LoggerFactory.getLogger(SkywalkingClient::class.java)

        fun registerCodecs(vertx: Vertx) {
            log.info("Registering Apache SkyWalking codecs")
            registerCodec(vertx, TraceResult::class.java)
            registerCodec(vertx, GetEndpointTraces::class.java)
            registerCodec(vertx, GetEndpointMetrics::class.java)
            registerCodec(vertx, GetAllServicesQuery.Result::class.java)
            registerCodec(vertx, GetServiceInstancesQuery.Result::class.java)
            registerCodec(vertx, SearchEndpointQuery.Result::class.java)
            registerCodec(vertx, QueryBasicTracesQuery.Result::class.java)
            registerCodec(vertx, ArrayList::class.java) //todo: should likely wrap in object
        }

        private fun <T> registerCodec(vertx: Vertx, type: Class<T>) {
            vertx.eventBus().registerDefaultCodec(type, LocalMessageCodec(type))
        }
    }

    init {
        registerCodecs(vertx)
    }

    suspend fun queryTraceStack(
        traceId: String,
    ): QueryTraceQuery.Result? {
        val response = apolloClient.query(QueryTraceQuery(traceId)).toDeferred().await()

        //todo: throw error if failed
        return response.data!!.result
    }

    suspend fun queryBasicTraces(
        endpointId: String,
        duration: Duration,
    ): QueryBasicTracesQuery.Result? {
        val response = apolloClient.query(
            QueryBasicTracesQuery(
                TraceQueryCondition(
                    endpointId = Input.optional(endpointId),
                    queryDuration = Input.optional(duration),
                    queryOrder = QueryOrder.BY_START_TIME, //todo: move to request
                    traceState = TraceState.ALL, //todo: move to request
                    paging = Pagination(pageSize = 10) //todo: move to request
                )
            )
        ).toDeferred().await()

        //todo: throw error if failed
        return response.data!!.result
    }

    suspend fun getEndpointMetrics(
        metricName: String,
        endpointId: String,
        duration: Duration
    ): GetLinearIntValuesQuery.Result? {
        val response = apolloClient.query(
            GetLinearIntValuesQuery(MetricCondition(metricName, Input.optional(endpointId)), duration)
        ).toDeferred().await()

        //todo: throw error if failed
        return response.data!!.result
    }

    suspend fun searchEndpoint(keyword: String, serviceId: String, limit: Int): List<SearchEndpointQuery.Result> {
        val response = apolloClient.query(
            SearchEndpointQuery(keyword, serviceId, limit)
        ).toDeferred().await()

        //todo: throw error if failed
        return response.data!!.result
    }

    suspend fun getServices(duration: Duration): List<GetAllServicesQuery.Result> {
        val response = apolloClient.query(
            GetAllServicesQuery(duration)
        ).toDeferred().await()

        //todo: throw error if failed
        return response.data!!.result
    }

    suspend fun getServiceInstances(serviceId: String, duration: Duration): List<GetServiceInstancesQuery.Result> {
        val response = apolloClient.query(
            GetServiceInstancesQuery(serviceId, duration)
        ).toDeferred().await()

        //todo: throw error if failed
        return response.data!!.result
    }

    fun getDuration(since: ZonedDateTime, step: DurationStep): Duration {
        return getDuration(since, ZonedDateTime.now(), step)
    }

    fun getDuration(from: ZonedDateTime, to: ZonedDateTime, step: DurationStep): Duration {
        val fromDate = from.withZoneSameInstant(ofHours(timezoneOffset))
        val toDate = to.withZoneSameInstant(ofHours(timezoneOffset))
        return Duration(
            fromDate.format(step.dateTimeFormatter),
            toDate.format(step.dateTimeFormatter),
            Step.valueOf(step.name)
        )
    }

    enum class DurationStep(val dateTimeFormatter: DateTimeFormatter) {
        DAY(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        HOUR(DateTimeFormatter.ofPattern("yyyy-MM-dd HH")),
        MINUTE(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")),
        SECOND(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmmss"))
    }
}