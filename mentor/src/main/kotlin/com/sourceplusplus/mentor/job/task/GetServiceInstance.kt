package com.sourceplusplus.mentor.job.task

import com.sourceplusplus.mentor.MentorJob
import com.sourceplusplus.mentor.MentorTask
import com.sourceplusplus.monitor.skywalking.track.ServiceInstanceTracker.Companion.getActiveServiceInstances
import com.sourceplusplus.monitor.skywalking.track.ServiceInstanceTracker.Companion.getCurrentServiceInstance
import monitor.skywalking.protocol.metadata.GetServiceInstancesQuery

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class GetServiceInstance(
    private val byId: String? = null,
    private val byName: String? = null,
    private val current: Boolean = true
) : MentorTask() {

    companion object {
        val SERVICE_INSTANCE: ContextKey<GetServiceInstancesQuery.Result> = ContextKey()
    }

    override suspend fun executeTask(job: MentorJob, context: TaskContext) {
        val service = context.get(GetService.SERVICE)
        if (current) {
            val serviceInstance = getCurrentServiceInstance(service.id, job.vertx)
            if (serviceInstance != null && isMatch(serviceInstance)) {
                context.put(SERVICE_INSTANCE, serviceInstance)
            }
        } else {
            for (serviceInstance in getActiveServiceInstances(service.id, job.vertx)) {
                if (isMatch(serviceInstance)) {
                    context.put(SERVICE_INSTANCE, serviceInstance)
                    break
                }
            }
        }
    }

    private fun isMatch(result: GetServiceInstancesQuery.Result): Boolean {
        return when {
            byId != null && byName != null && byId == result.id && byName == result.name -> true
            byId != null && byId == result.id -> true
            byName != null && byName == result.name -> true
            else -> false
        }
    }
}