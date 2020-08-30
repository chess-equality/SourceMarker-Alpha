package com.sourceplusplus.marker.plugin

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
open class SourceMarkerStartupActivity : StartupActivity {

    override fun runActivity(project: Project) {
        DumbService.getInstance(project).runReadActionInSmartMode {
            ApplicationManager.getApplication().invokeLater {
                val editorManager = FileEditorManager.getInstance(project)
                editorManager.allEditors.forEach { editor ->
                    FileActivityListener.triggerFileOpened(editorManager, editor.file!!)
                }
            }
        }
    }
}