package com.sourceplusplus.protocol.advice

data class MethodAdvice(
    val adviceType: AdviceType,
    val firstDetected: Long, //todo: date/instant
    val lastDetected: Long //todo: date/instant
)