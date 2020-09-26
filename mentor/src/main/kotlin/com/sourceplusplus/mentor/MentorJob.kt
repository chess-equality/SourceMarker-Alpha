package com.sourceplusplus.mentor

import io.vertx.core.Vertx

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
abstract class MentorJob {

    abstract val vertx: Vertx
    open val config: MentorJobConfig = MentorJobConfig()
    abstract val tasks: List<MentorTask>
    val context = HashMap<String, Any>()

    fun log(msg: String) {
        println(msg)
    }

    //todo: if jobs share functionality then they should share tasks
    //todo: would need to search for duplicate tasks when setting up all jobs
}