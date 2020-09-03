package com.sourceplusplus.protocol.artifact

data class ArtifactQualifiedName(
    val identifier: String,
    val commitId: String,
    val type: ArtifactType,
    val operationName: String //todo: only method artifacts need
)