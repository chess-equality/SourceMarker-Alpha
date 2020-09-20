package com.sourceplusplus.portal.server.page

import com.sourceplusplus.portal.server.portal
import com.sourceplusplus.portal.server.template.*
import com.sourceplusplus.protocol.artifact.ArtifactConfigType.AUTO_SUBSCRIBE
import com.sourceplusplus.protocol.artifact.ArtifactConfigType.ENTRY_METHOD
import com.sourceplusplus.protocol.artifact.ArtifactInfoType.*
import com.sourceplusplus.protocol.artifact.trace.TraceOrderType.*
import com.sourceplusplus.protocol.portal.PageType.*
import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.stream.appendHTML

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class ConfigurationPage {
    fun renderPage(): String {
        return buildString {
            appendLine("<!DOCTYPE html>")
            appendHTML().portal {
                configurationPage("Configuration - SourceMarker") {
                    portalNav {
                        navItem(OVERVIEW)
                        navItem(TRACES) {
                            navSubItem(LATEST_TRACES, SLOWEST_TRACES, FAILED_TRACES)
                        }
                        navItem(CONFIGURATION, isActive = true)
                    }
                    configurationContent {
                        navBar(false) {
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
