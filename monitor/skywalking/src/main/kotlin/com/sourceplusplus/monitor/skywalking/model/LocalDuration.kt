package com.sourceplusplus.monitor.skywalking.model

import com.sourceplusplus.monitor.skywalking.SkywalkingClient
import monitor.skywalking.protocol.type.Duration
import java.time.LocalDateTime

data class LocalDuration(
    val start: LocalDateTime,
    val stop: LocalDateTime,
    val step: SkywalkingClient.DurationStep
) {
    fun toDuration(skywalkingClient: SkywalkingClient): Duration {
        return skywalkingClient.getDuration(start, stop, step)
    }
}