package com.sourceplusplus.mentor.api

import com.sourceplusplus.protocol.ArtifactQualifiedName
import com.sourceplusplus.protocol.advice.MethodAdvice

interface SourceMentor {

    fun getAllMethodAdvice(methodQualifiedName: ArtifactQualifiedName): List<MethodAdvice>
}