package com.sourceplusplus.mapper.api

import com.sourceplusplus.protocol.artifact.ArtifactQualifiedName

interface SourceMapper {

//    fun getCurrentMethodQualifiedName(methodQualifiedName: String, commitId: String): String
    fun getMethodQualifiedName(methodQualifiedName: ArtifactQualifiedName, targetCommitId: String): ArtifactQualifiedName
}