package com.sourceplusplus.portal.server.display.tabs.views

import com.sourceplusplus.portal.server.display.PortalUI
import com.sourceplusplus.protocol.artifact.ArtifactMetricResult
import com.sourceplusplus.protocol.portal.MetricType
import com.sourceplusplus.protocol.portal.QueryTimeFrame
import java.util.concurrent.ConcurrentHashMap

/**
 * Holds the current view for the Overview portal tab.
 *
 * @version 0.3.2
 * @since 0.2.0
 * @author <a href="mailto:brandon@srcpl.us">Brandon Fergerson</a>
 */
//@Canonical
class OverviewView(
    val portalUI: PortalUI
) {

    var metricResultCache = ConcurrentHashMap<String, MutableMap<QueryTimeFrame, ArtifactMetricResult>>()
    var timeFrame = QueryTimeFrame.LAST_5_MINUTES
    var activeChartMetric = MetricType.ResponseTime_Average

    fun cloneView(view: OverviewView) {
        metricResultCache = view.metricResultCache
        timeFrame = view.timeFrame
        activeChartMetric = view.activeChartMetric
    }

    fun cacheMetricResult(metricResult: ArtifactMetricResult) {
        metricResultCache.putIfAbsent(
            metricResult.artifactQualifiedName,
            ConcurrentHashMap<QueryTimeFrame, ArtifactMetricResult>()
        )
        metricResultCache[metricResult.artifactQualifiedName]!![metricResult.timeFrame] = metricResult
    }

    val metricResult: ArtifactMetricResult?
        get() = getMetricResult(portalUI.viewingPortalArtifact, timeFrame)

    fun getMetricResult(artifactQualifiedName: String, timeFrame: QueryTimeFrame): ArtifactMetricResult? {
        return metricResultCache[artifactQualifiedName]?.get(timeFrame)
    }
}
