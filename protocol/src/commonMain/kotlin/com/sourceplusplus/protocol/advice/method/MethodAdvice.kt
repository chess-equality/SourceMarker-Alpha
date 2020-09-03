package com.sourceplusplus.protocol.advice.method

import com.sourceplusplus.protocol.advice.AdviceType

data class MethodAdvice(
    val adviceType: AdviceType,
    val firstDetected: Long, //todo: date/instant
    val lastDetected: Long //todo: date/instant
)