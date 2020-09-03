package com.sourceplusplus.sourcemarker.activities

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.sourceplusplus.marker.plugin.SourceMarkerPlugin
import com.sourceplusplus.marker.plugin.SourceMarkerStartupActivity
import com.sourceplusplus.marker.source.mark.api.component.jcef.SourceMarkSingleJcefComponentProvider
import com.sourceplusplus.marker.source.mark.gutter.config.GutterMarkConfiguration
import com.sourceplusplus.sourcemarker.listeners.PluginSourceMarkEventListener
import java.awt.Dimension

class PluginSourceMarkerStartupActivity : SourceMarkerStartupActivity(), Disposable {

    override fun runActivity(project: Project) {
        SourceMarkerPlugin.addGlobalSourceMarkEventListener(PluginSourceMarkEventListener())

        val configuration = GutterMarkConfiguration()
        configuration.activateOnMouseHover = false
        configuration.activateOnKeyboardShortcut = true
        val componentProvider = SourceMarkSingleJcefComponentProvider()
        configuration.componentProvider = componentProvider
        componentProvider.defaultConfiguration.setComponentSize(Dimension(400, 340))
        componentProvider.defaultConfiguration.initialUrl = "https://jetbrains.com"
        SourceMarkerPlugin.configuration.defaultGutterMarkConfiguration = configuration

        //Disposer.register(project, this)
        super.runActivity(project)
    }

    override fun dispose() {
        if (SourceMarkerPlugin.enabled) {
            SourceMarkerPlugin.clearAvailableSourceFileMarkers()
        }
    }
}