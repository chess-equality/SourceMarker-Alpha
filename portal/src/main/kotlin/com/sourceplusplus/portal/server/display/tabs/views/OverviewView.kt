package com.sourceplusplus.portal.server.display.tabs.views

import com.sourceplusplus.portal.server.display.SourcePortal
import com.sourceplusplus.protocol.artifact.ArtifactMetricResult
import com.sourceplusplus.protocol.portal.MetricType
import com.sourceplusplus.protocol.portal.QueryTimeFrame
import java.util.concurrent.ConcurrentHashMap

/**
 * Holds the current view for the Overview portal tab.
 *
 * @since 0.0.1
 * @author <a href="mailto:bfergerson@apache.org">Brandon Fergerson</a>
 */
class OverviewView(
    val portal: SourcePortal
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
        get() = getMetricResult(portal.viewingPortalArtifact, timeFrame)

    fun getMetricResult(artifactQualifiedName: String, timeFrame: QueryTimeFrame): ArtifactMetricResult? {
        return metricResultCache[artifactQualifiedName]?.get(timeFrame)
    }
}
