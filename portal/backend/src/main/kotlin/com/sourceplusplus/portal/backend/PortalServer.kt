package com.sourceplusplus.portal.backend

import com.sourceplusplus.portal.PortalViewTracker
import com.sourceplusplus.portal.display.ConfigurationDisplay
import com.sourceplusplus.portal.display.OverviewDisplay
import com.sourceplusplus.portal.display.TracesDisplay
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
import kotlinx.html.*
import kotlinx.html.stream.appendHTML

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
            ctx.respond(buildString {
                appendLine("<!DOCTYPE html>")
                appendHTML().html {
                    head {
                        overviewHead("Overview - SourceMarker")
                        script {
                            src = "js/overview.js"
                        }
                        script {
                            src = "js/views/overview_view.js"
                        }
                    }
                    body("overflow_y_hidden") {
                        id = "body"
                    }
                }
            })
        }
    }

    private suspend fun getTraces(ctx: RoutingContext) {
        withContext(Dispatchers.Default) {
            ctx.respond(buildString {
                appendLine("<!DOCTYPE html>")
                appendHTML().html {
                    head {
                        overviewHead("Traces - SourceMarker")
                        script {
                            src = "js/traces.js"
                        }
                        script {
                            src = "js/views/traces_view.js"
                        }
                        script {
                            src = "themes/default/assets/all.min.js"
                        }
                    }
                    body {
                        id = "body"
                    }
                }
            })
        }
    }

    private suspend fun getConfiguration(ctx: RoutingContext) {
        withContext(Dispatchers.Default) {
            ctx.respond(buildString {
                appendLine("<!DOCTYPE html>")
                appendHTML().html {
                    head {
                        overviewHead("Configuration - SourceMarker")
                        script {
                            src = "js/configuration.js"
                        }
                        script {
                            src = "js/views/configuration_view.js"
                        }
                    }
                    body {
                        id = "body"
                    }
                }
            })
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

    private fun HEAD.commonHead(title: String) {
        meta {
            charset = "UTF-8"
        }
        title { +title }
        link {
            rel = "stylesheet"
            href = "semantic.min.css"
        }
        script {
            src = "jquery-3.5.1.min.js"
        }
        script {
            src = "portal_theme.js"
        }
        script {
            src = "semantic.min.js"
        }
        script {
            src = "moment.min.js"
        }
        script {
            src = "sockjs.min.js"
        }
        script {
            src = "vertx-eventbus.min.js"
        }
        script {
            src = "source_eventbus_bridge.js"
        }
        script {
            src = "frontend.js"
        }
    }

    private fun HEAD.overviewHead(title: String, block: (HEAD.() -> Unit)? = null) {
        commonHead(title)
        script {
            src = "echarts.min.js"
        }
        block?.let { it() }
    }

    private fun HEAD.tracesHead(title: String, block: (HEAD.() -> Unit)? = null) {
        commonHead(title)
        block?.let { it() }
    }

    private fun HEAD.configurationHead(title: String, block: (HEAD.() -> Unit)? = null) {
        commonHead(title)
        block?.let { it() }
    }
}