package com.sourceplusplus.monitor.skywalking

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import io.vertx.core.Vertx
import monitor.skywalking.protocol.metadata.GetAllServicesQuery
import monitor.skywalking.protocol.metadata.GetServiceInstancesQuery
import monitor.skywalking.protocol.type.Duration
import monitor.skywalking.protocol.type.Step
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class SkywalkingClient(
    private val vertx: Vertx,
    private val apolloClient: ApolloClient,
    private val timezoneOffset: Int = 0
) {

    companion object {
        private val log = LoggerFactory.getLogger(SkywalkingClient::class.java)
    }

    suspend fun getServices(duration: Duration): List<GetAllServicesQuery.Result> {
        val response = apolloClient.query(
            GetAllServicesQuery(duration)
        ).toDeferred().await()

        //todo: think about this
        apolloClient.query(GetAllServicesQuery(duration))
            .watcher().enqueueAndWatch(object : ApolloCall.Callback<GetAllServicesQuery.Data>() {
                override fun onFailure(e: ApolloException) {
//                exceptionSubject.onNext(e)
                    println(e)
                }

                override fun onResponse(response: Response<GetAllServicesQuery.Data>) {
//                repositoriesSubject.onNext(mapRepositoriesResponseToRepositories(response))
                    println(response)
                }

                override fun onStatusEvent(event: ApolloCall.StatusEvent) {
                    println(event)
                }
            })

        //todo: throw error if failed
        return response.data!!.result
    }

    suspend fun getServiceInstances(serviceId: String, duration: Duration): List<GetServiceInstancesQuery.Result> {
        val response = apolloClient.query(
            GetServiceInstancesQuery(serviceId, duration)
        ).toDeferred().await()

        //todo: think about this
        apolloClient.query(GetServiceInstancesQuery(serviceId, duration))
            .watcher().enqueueAndWatch(object : ApolloCall.Callback<GetServiceInstancesQuery.Data>() {
                override fun onFailure(e: ApolloException) {
//                exceptionSubject.onNext(e)
                    println(e)
                }

                override fun onResponse(response: Response<GetServiceInstancesQuery.Data>) {
//                repositoriesSubject.onNext(mapRepositoriesResponseToRepositories(response))
                    println(response)
                }

                override fun onStatusEvent(event: ApolloCall.StatusEvent) {
                    println(event)
                }
            })

        //todo: throw error if failed
        return response.data!!.result
    }

    fun getDuration(since: LocalDateTime, step: DurationStep): Duration {
        return getDuration(since, LocalDateTime.now(), step)
    }

    fun getDuration(from: LocalDateTime, to: LocalDateTime, step: DurationStep): Duration {
        val fromDate = from.atZone(ZoneOffset.ofHours(timezoneOffset))
        val toDate = to.atZone(ZoneOffset.ofHours(timezoneOffset))
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