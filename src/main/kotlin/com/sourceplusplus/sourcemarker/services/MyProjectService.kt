package com.sourceplusplus.sourcemarker.services

import com.intellij.openapi.project.Project
import com.sourceplusplus.sourcemarker.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
