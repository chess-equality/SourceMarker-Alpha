package com.sourceplusplus.sourcemarker.activities

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.guava.GuavaModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.sourceplusplus.marker.plugin.SourceMarkerPlugin
import com.sourceplusplus.marker.plugin.SourceMarkerStartupActivity
import com.sourceplusplus.marker.source.mark.api.component.api.config.ComponentSizeEvaluator
import com.sourceplusplus.marker.source.mark.api.component.api.config.SourceMarkComponentConfiguration
import com.sourceplusplus.marker.source.mark.api.component.jcef.SourceMarkSingleJcefComponentProvider
import com.sourceplusplus.marker.source.mark.api.component.jcef.config.BrowserLoadingListener
import com.sourceplusplus.marker.source.mark.api.component.jcef.config.SourceMarkJcefComponentConfiguration
import com.sourceplusplus.marker.source.mark.gutter.config.GutterMarkConfiguration
import com.sourceplusplus.monitor.skywalking.SkywalkingMonitor
import com.sourceplusplus.portal.backend.PortalServer
import com.sourceplusplus.portal.frontend.SourcePortal
import com.sourceplusplus.protocol.artifact.ArtifactMetricResult
import com.sourceplusplus.protocol.artifact.trace.TraceResult
import com.sourceplusplus.protocol.artifact.trace.TraceSpanStackQueryResult
import com.sourceplusplus.sourcemarker.listeners.PluginSourceMarkEventListener
import com.sourceplusplus.sourcemarker.listeners.PortalEventListener
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.ext.bridge.PermittedOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import org.slf4j.LoggerFactory
import java.awt.Dimension

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class PluginSourceMarkerStartupActivity : SourceMarkerStartupActivity(), Disposable {

    companion object {
        private val log = LoggerFactory.getLogger(PluginSourceMarkerStartupActivity::class.java)
        val vertx: Vertx = Vertx.vertx()

        fun registerCodecs(vertx: Vertx) {
            log.debug("Registering SourceMarker Protocol codecs")
            registerCodec(vertx, SourcePortal::class.java)
            registerCodec(vertx, ArtifactMetricResult::class.java)
            registerCodec(vertx, TraceResult::class.java)
            registerCodec(vertx, TraceSpanStackQueryResult::class.java)

            DatabindCodec.mapper().registerModule(GuavaModule())
            DatabindCodec.mapper().registerModule(Jdk8Module())
            DatabindCodec.mapper().registerModule(JavaTimeModule())
            DatabindCodec.mapper().propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            DatabindCodec.mapper().enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            DatabindCodec.mapper().enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
        }

        private fun <T> registerCodec(vertx: Vertx, type: Class<T>) {
            vertx.eventBus().registerDefaultCodec(type, LocalMessageCodec(type))
        }
    }

    init {
        registerCodecs(vertx)
    }

    override fun runActivity(project: Project) {
        if (ApplicationManager.getApplication().isUnitTestMode) {
            return //todo: change when integration tests are added
        }

        initPortal()
        initMarker()
        initMapper()
        initMonitor()
        initMentor()
        super.runActivity(project)
    }

    private fun initMonitor() {
        //todo: configurable
        val config = JsonObject().apply {
            put("graphql_endpoint", "http://localhost:12800/graphql")
        }
        vertx.deployVerticle(SkywalkingMonitor(), DeploymentOptions().setConfig(config))
    }

    private fun initMapper() {
        //todo: this
    }

    private fun initMentor() {
        //todo: this
    }

    private fun initPortal() {
        //todo: load portal config (custom themes, etc)
        vertx.deployVerticle(PortalServer())
        vertx.deployVerticle(PortalEventListener())

        //todo: portal should be connected to event bus without bridge
        val sockJSHandler = SockJSHandler.create(vertx)
        val portalBridgeOptions = SockJSBridgeOptions()
            .addInboundPermitted(PermittedOptions().setAddressRegex(".+"))
            .addOutboundPermitted(PermittedOptions().setAddressRegex(".+"))
        sockJSHandler.bridge(portalBridgeOptions)

        val router = Router.router(vertx)
        router.route("/eventbus/*").handler(sockJSHandler)
        vertx.createHttpServer().requestHandler(router).listen(8888, "localhost")
    }

    private fun initMarker() {
        SourceMarkerPlugin.addGlobalSourceMarkEventListener(PluginSourceMarkEventListener())

        val configuration = GutterMarkConfiguration()
        configuration.activateOnMouseHover = false
        configuration.activateOnKeyboardShortcut = true
        val componentProvider = SourceMarkSingleJcefComponentProvider().apply {
            defaultConfiguration.preloadJcefBrowser = false
            defaultConfiguration.componentSizeEvaluator = object : ComponentSizeEvaluator() {
                override fun getDynamicSize(
                    editor: Editor,
                    configuration: SourceMarkComponentConfiguration
                ): Dimension {
                    var portalWidth = (editor.contentComponent.width * 0.8).toInt()
                    if (portalWidth > 775) {
                        portalWidth = 775
                    }
                    return Dimension(portalWidth, 250)
                }
            }
            defaultConfiguration.browserLoadingListener = object : BrowserLoadingListener() {
                override fun beforeBrowserCreated(configuration: SourceMarkJcefComponentConfiguration) {
                    configuration.initialUrl =
                        "http://localhost:8080/overview?portal_uuid=${SourcePortal.getPortals()[0].portalUuid}"
                }
            }
        }
        configuration.componentProvider = componentProvider

        SourceMarkerPlugin.configuration.defaultGutterMarkConfiguration = configuration
    }

    override fun dispose() {
        if (SourceMarkerPlugin.enabled) {
            SourceMarkerPlugin.clearAvailableSourceFileMarkers()
        }
        vertx.close()
    }
}
