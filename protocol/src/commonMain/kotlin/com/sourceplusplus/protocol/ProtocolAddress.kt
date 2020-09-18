package com.sourceplusplus.protocol

class ProtocolAddress {
    class Global {
        companion object {
            const val OverviewTabOpened = "OverviewTabOpened"
            const val TracesTabOpened = "TracesTabOpened"
            const val ConfigurationTabOpened = "ConfigurationTabOpened"
            const val ClickedDisplayTraces = "ClickedDisplayTraces"
            const val ClickedDisplayTraceStack = "ClickedDisplayTraceStack"
            const val ClickedDisplaySpanInfo = "ClickedDisplaySpanInfo"
            const val ClickedViewAsExternalPortal = "ClickedViewAsExternalPortal"
        }
    }

    @Suppress("FunctionName")
    class Portal {
        companion object {
            fun ClearOverview(portalUuid: String): String {
                return "$portalUuid-ClearOverview"
            }

            fun UpdateChart(portalUuid: String): String {
                return "$portalUuid-UpdateChart"
            }

            fun DisplayCard(portalUuid: String): String {
                return "$portalUuid-DisplayCard"
            }

            fun DisplayTraces(portalUuid: String): String {
                return "$portalUuid-DisplayTraces"
            }

            fun DisplayTraceStack(portalUuid: String): String {
                return "$portalUuid-DisplayTraceStack"
            }

            fun DisplaySpanInfo(portalUuid: String): String {
                return "$portalUuid-DisplaySpanInfo"
            }

            fun DisplayArtifactConfiguration(portalUuid: String): String {
                return "$portalUuid-DisplayArtifactConfiguration"
            }
        }
    }
}