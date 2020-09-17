package com.sourceplusplus.portal.server.model.artifact

enum class ArtifactInfoType {
    QUALIFIED_NAME,
    CREATE_DATE,
    LAST_UPDATED,
    ENDPOINT;

    val id = name.toLowerCase()
    val description = id.split("_").joinToString(" ") { it.capitalize() }
}