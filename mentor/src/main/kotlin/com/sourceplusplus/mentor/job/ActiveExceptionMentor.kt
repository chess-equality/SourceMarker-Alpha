package com.sourceplusplus.mentor.job

import com.sourceplusplus.mentor.MentorJob
import com.sourceplusplus.mentor.MentorJobConfig
import com.sourceplusplus.mentor.MentorTask
import com.sourceplusplus.mentor.job.task.GetService
import com.sourceplusplus.mentor.job.task.GetServiceInstance

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class ActiveExceptionMentor(
    override val config: MentorJobConfig = MentorJobConfig()
) : MentorJob() {

    //todo: get active service instance
    //todo: find failing traces
    //todo: determine failing location
    //todo: create advice
    //todo: maintain created advice status (remove on new instances, etc)

    override val tasks: List<MentorTask>
        //todo: need way of passing
        get() {
            return listOf(
                GetService(),
                GetServiceInstance()
////                GetTraces(this, FAILED_TRACES, LAST_15_MINUTES)
            )
        }
}