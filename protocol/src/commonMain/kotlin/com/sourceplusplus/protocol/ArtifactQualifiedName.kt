package com.sourceplusplus.protocol

data class ArtifactQualifiedName(
    val identifier: String,
    val commitId: String,
    val type: ArtifactType
)