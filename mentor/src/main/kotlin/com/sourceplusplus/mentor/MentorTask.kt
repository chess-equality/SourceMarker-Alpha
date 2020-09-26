package com.sourceplusplus.mentor

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
abstract class MentorTask : Comparable<MentorTask> {

    var priority: Int = 0
        set(value) {
            field = value
            //todo: remove/add to queue
        }

    abstract fun executeTask(job: MentorJob, context: TaskContext)

    override operator fun compareTo(other: MentorTask): Int = priority.compareTo(other.priority)

    interface TaskContext {

    }
}