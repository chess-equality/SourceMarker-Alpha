package com.sourceplusplus.portal.backend

import com.sourceplusplus.portal.backend.page.ConfigurationPage
import com.sourceplusplus.portal.backend.page.OverviewPage
import com.sourceplusplus.portal.backend.page.TracesPage
import com.sourceplusplus.portal.frontend.PortalViewTracker
import com.sourceplusplus.portal.frontend.display.ConfigurationDisplay
import com.sourceplusplus.portal.frontend.display.OverviewDisplay
import com.sourceplusplus.portal.frontend.display.TracesDisplay
import io.netty.buffer.Unpooled
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.ext.web.handler.ResponseTimeHandler
import io.vertx.kotlin.core.deployVerticleAwait
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.html.HTML
import kotlinx.html.TagConsumer
import kotlinx.html.visitAndFinalize

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class PortalServer : CoroutineVerticle() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val vertx: Vertx = Vertx.vertx()
            vertx.deployVerticle(PortalServer::class.java.name)
        }
    }

    override suspend fun start() {
        vertx.deployVerticleAwait(OverviewDisplay())
        vertx.deployVerticleAwait(TracesDisplay())
        vertx.deployVerticleAwait(ConfigurationDisplay(false)) //todo: dynamic
        vertx.deployVerticleAwait(PortalViewTracker())

        // Build Vert.x Web router
        val router = Router.router(vertx)
        router.route().handler(LoggerHandler.create())
        router.route().handler(ResponseTimeHandler.create())

//        // Static handler
//        router.route("/*").handler(StaticHandler.create())
        // Routes
        router.get("/").coroutineHandler { ctx -> getOverview(ctx) } //todo: could make whole application overview
        router.get("/overview").coroutineHandler { ctx -> getOverview(ctx) }
        router.get("/traces").coroutineHandler { ctx -> getTraces(ctx) }
        router.get("/configuration").coroutineHandler { ctx -> getConfiguration(ctx) }

        // Static handler
        router.get("/*").handler {
            val fileStream = PortalServer::class.java.classLoader.getResourceAsStream("webroot" + it.request().path())
            it.response().setStatusCode(200).end(Buffer.buffer(Unpooled.copiedBuffer(fileStream!!.readAllBytes())))
            //todo: add cache headers
        }

        // Start the server
        vertx.createHttpServer()
            .requestHandler(router)
            .listenAwait(config.getInteger("http.port", 8080))
    }

    private suspend fun getOverview(ctx: RoutingContext) {
        withContext(Dispatchers.Default) {
            ctx.respond(OverviewPage().renderPage())
        }
    }

    private suspend fun getTraces(ctx: RoutingContext) {
        withContext(Dispatchers.Default) {
            ctx.respond(TracesPage().renderPage())
        }
    }

    private suspend fun getConfiguration(ctx: RoutingContext) {
        withContext(Dispatchers.Default) {
            ctx.respond(ConfigurationPage().renderPage())
        }
    }

    private fun RoutingContext.respond(respondBody: String = "", status: Int = 200) {
        response()
            .putHeader("content-type", "text/html")
            .setStatusCode(status)
            .end(respondBody)
    }

    private fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit) {
        handler { ctx ->
            launch(ctx.vertx().dispatcher()) {
                try {
                    fn(ctx)
                } catch (e: Exception) {
                    ctx.fail(e)
                }
            }
        }
    }
}

fun <T, C : TagConsumer<T>> C.portal(namespace: String? = null, block: HTML.() -> Unit = {}):
        T = HTML(kotlinx.html.emptyMap, this, namespace).visitAndFinalize(this, block)