package com.sourceplusplus.sourcemarker.activities

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.sourceplusplus.marker.plugin.SourceMarkerPlugin
import com.sourceplusplus.marker.plugin.SourceMarkerStartupActivity
import com.sourceplusplus.marker.source.mark.api.component.jcef.SourceMarkJcefComponentProvider
import com.sourceplusplus.marker.source.mark.gutter.config.GutterMarkConfiguration
import java.awt.Dimension

class PluginStartupActivity : SourceMarkerStartupActivity(), Disposable {

    override fun runActivity(project: Project) {
//        Disposer.register(project, this)

        val configuration = GutterMarkConfiguration()
        configuration.activateOnMouseHover = false
        configuration.activateOnKeyboardShortcut = true
        //configuration.setIcon(GutterMarkIcon.globe);

        val componentProvider: SourceMarkJcefComponentProvider =
            configuration.componentProvider as SourceMarkJcefComponentProvider
        componentProvider.defaultConfiguration.setComponentSize(Dimension(400, 340))
        componentProvider.defaultConfiguration.initialUrl = "https://jetbrains.com"
        SourceMarkerPlugin.configuration.defaultGutterMarkConfiguration = configuration

        super.runActivity(project)
    }

    override fun dispose() {
        if (SourceMarkerPlugin.enabled) {
            SourceMarkerPlugin.clearAvailableSourceFileMarkers()
        }
    }
}