package com.sourceplusplus.monitor.skywalking

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import monitor.skywalking.protocol.metadata.GetAllServicesQuery
import monitor.skywalking.protocol.metadata.GetServiceInstancesQuery
import monitor.skywalking.protocol.metadata.SearchEndpointQuery
import monitor.skywalking.protocol.type.Duration
import monitor.skywalking.protocol.type.Step
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.ZoneOffset.ofHours
import java.time.ZoneOffset.systemDefault
import java.time.ZonedDateTime.of
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
            registerCodec(vertx, GetAllServicesQuery.Result::class.java)
            registerCodec(vertx, GetServiceInstancesQuery.Result::class.java)
            registerCodec(vertx, SearchEndpointQuery.Result::class.java)
            registerCodec(vertx, ArrayList::class.java)
        }

        private fun <T> registerCodec(vertx: Vertx, type: Class<T>) {
            vertx.eventBus().registerDefaultCodec(type, SkywalkingMessageCodec(type))
        }
    }

    init {
        registerCodecs(vertx)
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

    fun getDuration(since: LocalDateTime, step: DurationStep): Duration {
        return getDuration(since, LocalDateTime.now(), step)
    }

    fun getDuration(from: LocalDateTime, to: LocalDateTime, step: DurationStep): Duration {
        val fromDate = of(from, systemDefault()).withZoneSameInstant(ofHours(timezoneOffset))
        val toDate = of(to, systemDefault()).withZoneSameInstant(ofHours(timezoneOffset))
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

    class SkywalkingMessageCodec<T> internal constructor(private val type: Class<T>) : MessageCodec<T, T> {
        override fun encodeToWire(buffer: Buffer, o: T) {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun decodeFromWire(pos: Int, buffer: Buffer): T {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun transform(o: T): T {
            return o
        }

        override fun name(): String {
            return UUID.randomUUID().toString()
        }

        override fun systemCodecID(): Byte {
            return -1
        }

        fun type(): Class<T> {
            return type
        }
    }
}