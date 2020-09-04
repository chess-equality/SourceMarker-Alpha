package com.sourceplusplus.sourcemarker.activities

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.sourceplusplus.marker.plugin.SourceMarkerPlugin
import com.sourceplusplus.marker.plugin.SourceMarkerStartupActivity
import com.sourceplusplus.marker.source.mark.api.component.api.config.ComponentSizeEvaluator
import com.sourceplusplus.marker.source.mark.api.component.api.config.SourceMarkComponentConfiguration
import com.sourceplusplus.marker.source.mark.api.component.jcef.SourceMarkSingleJcefComponentProvider
import com.sourceplusplus.marker.source.mark.gutter.config.GutterMarkConfiguration
import com.sourceplusplus.portal.server.PortalServer
import com.sourceplusplus.sourcemarker.listeners.PluginSourceMarkEventListener
import io.vertx.core.Vertx
import java.awt.Dimension

class PluginSourceMarkerStartupActivity : SourceMarkerStartupActivity(), Disposable {

    private val vertx: Vertx = Vertx.vertx()

    override fun runActivity(project: Project) {
        initPortal()
        initMarker()

        super.runActivity(project)
    }

    private fun initPortal() {
        vertx.deployVerticle(PortalServer())
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
        }
        configuration.componentProvider = componentProvider

        componentProvider.defaultConfiguration.initialUrl = "http://localhost:8080/overview"
        SourceMarkerPlugin.configuration.defaultGutterMarkConfiguration = configuration
    }

    override fun dispose() {
        if (SourceMarkerPlugin.enabled) {
            SourceMarkerPlugin.clearAvailableSourceFileMarkers()
        }
        vertx.close()
    }
}