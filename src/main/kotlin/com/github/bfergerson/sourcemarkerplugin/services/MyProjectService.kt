package com.github.bfergerson.sourcemarkerplugin.services

import com.intellij.openapi.project.Project
import com.github.bfergerson.sourcemarkerplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
