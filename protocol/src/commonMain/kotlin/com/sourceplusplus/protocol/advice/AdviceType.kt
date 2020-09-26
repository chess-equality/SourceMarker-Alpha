package com.sourceplusplus.protocol.advice

/**
 * Types of advice assignable to a source code artifact.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
enum class AdviceType {

    /**
     * Advice which is useful or interesting.
     */
    INFORMATIVE,

    /**
     * Advice which indicates development action is required.
     */
    CAUTIONARY
}
