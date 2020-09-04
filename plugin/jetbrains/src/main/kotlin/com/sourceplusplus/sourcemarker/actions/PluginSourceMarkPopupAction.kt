package com.sourceplusplus.sourcemarker.actions

import com.intellij.openapi.editor.Editor
import com.sourceplusplus.marker.source.mark.SourceMarkPopupAction
import com.sourceplusplus.marker.source.mark.api.SourceMark
import com.sourceplusplus.marker.source.mark.api.component.jcef.SourceMarkJcefComponent
import java.util.concurrent.ThreadLocalRandom

class PluginSourceMarkPopupAction : SourceMarkPopupAction() {

    override fun performPopupAction(sourceMark: SourceMark, editor: Editor) {
        //todo: use SourcePortalAPI to ensure correct view is showing
        val jcefComponent = sourceMark.sourceMarkComponent as SourceMarkJcefComponent
        if (ThreadLocalRandom.current().nextBoolean()) {
            jcefComponent.getBrowser().cefBrowser.executeJavaScript(
                """
                  window.location.href = 'http://localhost:8080/configuration';
            """.trimIndent(), "", 0
            )
        } else {
            jcefComponent.getBrowser().cefBrowser.executeJavaScript(
                """
                  window.location.href = 'http://localhost:8080/traces';
            """.trimIndent(), "", 0
            )
        }

        super.performPopupAction(sourceMark, editor)
    }
}