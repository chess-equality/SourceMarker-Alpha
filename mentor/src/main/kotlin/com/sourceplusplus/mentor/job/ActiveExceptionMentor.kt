package com.sourceplusplus.mentor.job

import com.sourceplusplus.mentor.MentorJob
import com.sourceplusplus.mentor.MentorTask
import com.sourceplusplus.mentor.job.task.GetServiceInstance

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class ActiveExceptionMentor : MentorJob() {

    //todo: get active service instance
    //todo: find failing traces
    //todo: determine failing location
    //todo: create advice
    //todo: maintain created advice status (remove on new instances, etc)

    override val tasks: List<MentorTask>
        get() {
            return listOf(
                GetServiceInstance(this)
            )
        }
}