package com.sourceplusplus.portal.api

import com.sourceplusplus.portal.server.model.MetricType
import com.sourceplusplus.portal.server.model.QueryTimeFrame
import com.sourceplusplus.portal.server.model.SplineChart
import com.sourceplusplus.portal.server.model.SplineSeriesData
import java.time.Instant

//todo: or maybe PortalController
interface PortalAPI {

    fun loadPage()

    fun displayChart(splineChart: SplineChart) {
        val seriesData = SplineSeriesData(0, listOf(Instant.now(), Instant.now().plusSeconds(10)), doubleArrayOf(10.0, 10.0))
        val splineChart = SplineChart(MetricType.ResponseTime_Average, QueryTimeFrame.LAST_15_MINUTES, listOf(seriesData))
        //vertx.eventBus().publish("1-UpdateChart", JsonObject(Json.encode(splineChart)))
    }
}