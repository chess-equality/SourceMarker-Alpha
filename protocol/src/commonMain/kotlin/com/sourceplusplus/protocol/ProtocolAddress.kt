package com.sourceplusplus.protocol

class ProtocolAddress {
    class Global {
        companion object {
            //Portal
            const val ClosePortal = "ClosePortal"
            const val ClickedViewAsExternalPortal = "ClickedViewAsExternalPortal"
            const val KeepAlivePortal = "KeepAlivePortal"
            const val UpdatePortalArtifact = "UpdatePortalArtifact"
            const val CanOpenPortal = "CanOpenPortal"
            const val OpenedPortal = "OpenedPortal"
            const val ClosedPortal = "ClosedPortal"
            const val ChangedPortalArtifact = "ChangedPortalArtifact"

            //Portal - Overview
            const val OverviewTabOpened = "OverviewTabOpened"
            const val SetMetricTimeFrame = "SetMetricTimeFrame"
            const val SetActiveChartMetric = "SetActiveChartMetric"
            const val RefreshOverview = "RefreshOverview"
            const val ArtifactMetricUpdated = "ArtifactMetricUpdated"

            //Portal - Traces
            const val TracesTabOpened = "TracesTabOpened"
            const val ClickedDisplayTraces = "ClickedDisplayTraces"
            const val ClickedDisplayTraceStack = "ClickedDisplayTraceStack"
            const val ClickedDisplaySpanInfo = "ClickedDisplaySpanInfo"
            const val GetTraceStack = "GetTraceStack"

            //Portal - Configuration
            const val ConfigurationTabOpened = "ConfigurationTabOpened"
            const val DisplayArtifactConfiguration = "DisplayArtifactConfiguration"
            const val UpdateArtifactEntryMethod = "UpdateArtifactEntryMethod"
            const val UpdateArtifactAutoSubscribe = "UpdateArtifactAutoSubscribe"
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