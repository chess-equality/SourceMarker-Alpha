package com.sourceplusplus.monitor.skywalking

import monitor.skywalking.protocol.metrics.GetLinearIntValuesQuery
import java.math.BigDecimal

fun GetLinearIntValuesQuery.Result.toDoubleArray(): DoubleArray {
    return values.map { (it.value as BigDecimal).toDouble() }.toDoubleArray()
}