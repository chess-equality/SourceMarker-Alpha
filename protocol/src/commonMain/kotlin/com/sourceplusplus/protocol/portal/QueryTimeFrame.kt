package com.sourceplusplus.protocol.portal

/**
 * todo: description
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
enum class QueryTimeFrame(val minutes: Int) {
    LAST_5_MINUTES(5),
    LAST_15_MINUTES(15),
    LAST_30_MINUTES(30),
    LAST_HOUR(60),
    LAST_3_HOURS(60 * 3);

    companion object {
        fun valueOf(minutes: Int): QueryTimeFrame {
            for (timeFrame in values()) {
                if (timeFrame.minutes == minutes) {
                    return timeFrame
                }
            }
            throw IllegalArgumentException("No time frame for minutes: $minutes")
        }
    }
}