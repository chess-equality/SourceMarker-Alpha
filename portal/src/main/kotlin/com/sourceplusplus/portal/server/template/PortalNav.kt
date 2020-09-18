package com.sourceplusplus.portal.server.template

import com.sourceplusplus.portal.server.model.PageType
import com.sourceplusplus.portal.server.model.trace.TraceOrderType
import kotlinx.html.*

class PortalNavigationConfiguration(private val flowContent: FlowContent) {

    internal enum class ModeType {
        MENU,
        TAB
    }

    val menuItems = ArrayList<() -> Unit>()
    val tabItems = ArrayList<() -> Unit>()
    private var mode = ModeType.MENU

    fun setMenuMode() {
        mode = ModeType.MENU
    }

    fun setTabMode() {
        mode = ModeType.TAB
    }

    fun navItem(pageType: PageType, isActive: Boolean = false, block: (FlowContent.() -> Unit)? = null) {
        menuItems.add {
            flowContent.menuItem(pageType, isActive, block)
        }
        tabItems.add {
            flowContent.tabItem(pageType, isActive, block)
        }
    }

    fun navSubItem(vararg traceOrderTypes: TraceOrderType = arrayOf()) {
        when (mode) {
            ModeType.MENU -> flowContent.subMenuItem(*traceOrderTypes)
            ModeType.TAB -> flowContent.subTabItem(*traceOrderTypes)
        }
    }
}

fun FlowContent.portalNav(block: PortalNavigationConfiguration.() -> Unit) {
    div("ui sidebar vertical left menu overlay visible very thin icon spp_blue webkit_transition") {
        style = "overflow: visible !important;"
        val portalNavigation = PortalNavigationConfiguration(this).apply(block)
        portalNavigation.setMenuMode()
        menu {
            portalNavigation.menuItems.forEach {
                it.invoke()
            }
        }
        portalNavigation.setTabMode()
        tabs {
            portalNavigation.tabItems.forEach {
                it.invoke()
            }
        }
    }
}