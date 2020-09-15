package com.sourceplusplus.portal.server.page

import com.sourceplusplus.portal.server.portal
import com.sourceplusplus.portal.server.template.*
import com.sourceplusplus.portal.server.model.PageType.*
import com.sourceplusplus.portal.server.model.TraceType.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML

class ConfigurationPage {
    fun renderPage(): String {
        return buildString {
            appendLine("<!DOCTYPE html>")
            appendHTML().portal {
                configurationPage {
                    leftNav {
                        menu {
                            menuItem(OVERVIEW) {}
                            menuItem(TRACES) {
                                subMenuItem(LATEST, SLOWEST, FAILED)
                            }
                            menuItem(CONFIGURATION, isActive = true) {}
                        }
                        sidebar {
                            tabs {
                                tabItem(OVERVIEW) {}
                                tabItem(TRACES) { activeClass ->
                                    subTabItem(activeClass, LATEST, SLOWEST, FAILED)
                                }
                                tabItem(CONFIGURATION, isActive = true) {}
                            }
                        }
                    }
                    configurationContent {
                        configurationNavBar {
                            rightAlign {
                                externalPortalButton()
                            }
                        }
                        configurationTable {
                            artifactConfiguration()
                            artifactInformation()
                        }
                    }
                    configurationScripts()
                }
            }
        }
    }
}

fun HTML.configurationPage(title: String = "Configuration - Source++", bodyClass: String = "", block: FlowContent.() -> Unit) {
    head {
        configurationHead(title) {}
    }
    body(bodyClass) {
        block()
    }
}