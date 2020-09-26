package com.sourceplusplus.mentor.job

import com.sourceplusplus.mentor.MentorJob
import com.sourceplusplus.mentor.MentorJobConfig
import com.sourceplusplus.mentor.MentorTask
import io.vertx.core.Vertx

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class RampDetectionMentor(
    override val vertx: Vertx,
    override val config: MentorJobConfig = MentorJobConfig()
) : MentorJob() {

    //todo: get active service instance
    //todo: find endpoints with consistently increasing response time of a certain threshold
    //todo: search source code of endpoint for culprits

    override val tasks: List<MentorTask>
        get() = TODO("Not yet implemented")
}