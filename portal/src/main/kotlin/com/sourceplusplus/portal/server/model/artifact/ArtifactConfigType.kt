package com.sourceplusplus.portal.server.model.artifact

enum class ArtifactConfigType(val description: String) {
    ENTRY_METHOD("Entry method"),
    AUTO_SUBSCRIBE("Auto-subscribe");

    val id = name.toLowerCase()
}