package com.sourceplusplus.mentor.api

import com.sourceplusplus.protocol.artifact.ArtifactQualifiedName
import com.sourceplusplus.protocol.advice.method.MethodAdvice

interface SourceMentor {

    fun getAllMethodAdvice(methodQualifiedName: ArtifactQualifiedName): List<MethodAdvice>
}