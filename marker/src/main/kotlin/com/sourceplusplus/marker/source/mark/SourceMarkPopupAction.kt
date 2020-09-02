package com.sourceplusplus.marker.source.mark

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.sourceplusplus.marker.source.SourceFileMarker
import com.sourceplusplus.marker.source.mark.api.MethodSourceMark
import com.sourceplusplus.marker.source.mark.api.SourceMark
import org.jetbrains.annotations.NotNull

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.1.4
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
open class SourceMarkPopupAction : AnAction() {

    override fun update(@NotNull e: AnActionEvent) {
        val project: Project? = e.project
        e.presentation.isEnabledAndVisible = project != null
    }

    override fun actionPerformed(@NotNull e: AnActionEvent) {
        val project: Project? = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        if (project != null && editor != null && psiFile != null) {
            var sourceMark: SourceMark? = null
            val fileMarker = psiFile.getUserData(SourceFileMarker.KEY)
            if (fileMarker != null) {
                sourceMark = fileMarker.getSourceMarks().find {
                    if (it is MethodSourceMark) {
                        if (it.configuration.activateOnKeyboardShortcut) {
                            //+1 on end offset so match is made even right after method end
                            val incTextRange = TextRange(it.psiMethod.sourcePsi!!.textRange.startOffset,
                                    it.psiMethod.sourcePsi!!.textRange.endOffset + 1)
                            incTextRange.contains(editor.logicalPositionToOffset(editor.caretModel.logicalPosition))
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                }
            }

            if (sourceMark != null) {
                performPopupAction(sourceMark, editor)
            }
        }
    }

    open fun performPopupAction(sourceMark: SourceMark, editor: Editor) {
        sourceMark.displayPopup(editor)
    }
}