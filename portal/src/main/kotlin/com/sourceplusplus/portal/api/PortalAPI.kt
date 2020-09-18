package com.sourceplusplus.portal.api

import com.sourceplusplus.protocol.portal.MetricType
import com.sourceplusplus.protocol.portal.QueryTimeFrame
import com.sourceplusplus.protocol.portal.SplineChart
import com.sourceplusplus.protocol.portal.SplineSeriesData
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone.Companion.UTC
import kotlinx.datetime.plus

//todo: or maybe PortalController
interface PortalAPI {

    fun loadPage()

    fun displayChart(splineChart: SplineChart) {
        val seriesData = SplineSeriesData(
            0,
            listOf(
                Clock.System.now().toEpochMilliseconds(),
                Clock.System.now().plus(10, DateTimeUnit.SECOND, UTC).toEpochMilliseconds()
            ),
            doubleArrayOf(10.0, 10.0)
        )
        val splineChart =
            SplineChart(MetricType.ResponseTime_Average, QueryTimeFrame.LAST_15_MINUTES, listOf(seriesData))
        //vertx.eventBus().publish("1-UpdateChart", JsonObject(Json.encode(splineChart)))
    }
}