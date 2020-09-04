package com.sourceplusplus.monitor.skywalking

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import monitor.skywalking.protocol.metadata.GetTimeInfoQuery
import org.slf4j.LoggerFactory

class SkywalkingMonitor : AbstractVerticle() {

    companion object {
        private val log = LoggerFactory.getLogger(SkywalkingMonitor::class.java)
    }

    private lateinit var skywalkingClient: ApolloClient
    private var timezone: Int = 0

    override fun start(startPromise: Promise<Void>) {
        setup()
        startPromise.complete()
    }

    private fun setup() = runBlocking {
        skywalkingClient = ApolloClient.builder()
            .serverUrl(config().getString("graphql_endpoint"))
            .build()

        val query = launch {
            val response = skywalkingClient.query(GetTimeInfoQuery()).toDeferred().await()
            if (response.hasErrors()) {
                log.error("Failed to get Apache SkyWalking time info. Response: $response")
                return@launch //todo: throw error
            } else {
                timezone = Integer.parseInt(response.data!!.result!!.timezone)
            }
        }
        query.join()
    }
}