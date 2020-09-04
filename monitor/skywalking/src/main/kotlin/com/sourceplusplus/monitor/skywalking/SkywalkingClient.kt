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
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class SkywalkingClient(
    private val vertx: Vertx,
    private val apolloClient: ApolloClient,
    private val timezone: Int = 0
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

    fun getDuration(since: LocalDateTime): Duration {
        return getDuration(since, LocalDateTime.now())
    }

    fun getDuration(from: LocalDateTime, to: LocalDateTime): Duration {
        TODO("This")
    }
}