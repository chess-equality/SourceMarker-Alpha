package com.sourceplusplus.portal.server.page

import com.sourceplusplus.portal.server.portal
import com.sourceplusplus.portal.server.template.*
import com.sourceplusplus.portal.server.model.PageType.*
import com.sourceplusplus.portal.server.model.trace.TraceType.*
import com.sourceplusplus.portal.server.model.artifact.ArtifactConfigType.*
import com.sourceplusplus.portal.server.model.artifact.ArtifactInfoType.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML

class ConfigurationPage {
    fun renderPage(): String {
        return buildString {
            appendLine("<!DOCTYPE html>")
            appendHTML().portal {
                configurationPage("Configuration - Source++") {
                    portalNav {
                        navItem(OVERVIEW)
                        navItem(TRACES) {
                            navSubItem(LATEST, SLOWEST, FAILED)
                        }
                        navItem(CONFIGURATION, isActive = true)
                    }
                    configurationContent {
                        configurationNavBar {
                            rightAlign {
                                externalPortalButton()
                            }
                        }
                        configurationTable {
                            artifactConfiguration(ENTRY_METHOD, AUTO_SUBSCRIBE)
                            artifactInformation(QUALIFIED_NAME, CREATE_DATE, LAST_UPDATED, ENDPOINT)
                        }
                    }
                    configurationScripts()
                }
            }
        }
    }
}

fun HTML.configurationPage(title: String, block: FlowContent.() -> Unit) {
    head {
        configurationHead(title)
    }
    body {
        block()
    }
}