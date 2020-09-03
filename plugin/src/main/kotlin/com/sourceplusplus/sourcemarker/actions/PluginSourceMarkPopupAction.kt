package com.sourceplusplus.sourcemarker.actions

import com.intellij.openapi.editor.Editor
import com.sourceplusplus.marker.source.mark.SourceMarkPopupAction
import com.sourceplusplus.marker.source.mark.api.SourceMark

class PluginSourceMarkPopupAction : SourceMarkPopupAction() {

    override fun performPopupAction(sourceMark: SourceMark, editor: Editor) {
        //todo: use SourcePortalAPI to ensure correct view is showing
        super.performPopupAction(sourceMark, editor)
    }
}