import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import monitor.skywalking.protocol.metadata.GetTimeInfoQuery
import org.slf4j.LoggerFactory

class SourceMentorImpl {

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
}