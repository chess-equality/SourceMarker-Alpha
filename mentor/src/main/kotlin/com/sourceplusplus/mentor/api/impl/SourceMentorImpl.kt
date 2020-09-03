package com.sourceplusplus.mentor.api.impl

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.sourceplusplus.mentor.api.SourceMentor
import com.sourceplusplus.protocol.artifact.ArtifactQualifiedName
import com.sourceplusplus.protocol.advice.method.MethodAdvice
import com.sourceplusplus.protocol.skywalking.metadata.GetTimeInfoQuery
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

class SourceMentorImpl : SourceMentor {

    companion object {
        private val log = LoggerFactory.getLogger(SourceMentorImpl::class.java)
    }

    private lateinit var skywalkingClient: ApolloClient
    private var timezone: Int = 0

    init {
        setup()
    }

    private fun setup() = runBlocking {
        skywalkingClient = ApolloClient.builder()
            .serverUrl("http://localhost:12800/graphql")
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

    override fun getAllMethodAdvice(methodQualifiedName: ArtifactQualifiedName): List<MethodAdvice> {
        return listOf()
    }
}