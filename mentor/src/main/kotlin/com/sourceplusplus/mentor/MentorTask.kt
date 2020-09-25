package com.sourceplusplus.mentor

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
open class MentorTask : Comparable<MentorTask> {

    var priority: Int = 0
        set(value) {
            field = value
            //todo: remove/add to queue
        }

    override fun compareTo(other: MentorTask): Int = priority.compareTo(other.priority)
}