package com.sourceplusplus.protocol.advice.method

import com.sourceplusplus.protocol.advice.AdviceType

/**
 * todo: description
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
data class MethodAdvice(
    val adviceType: AdviceType,
    val firstDetected: Long, //todo: date/instant
    val lastDetected: Long //todo: date/instant
)