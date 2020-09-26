package com.sourceplusplus.mentor

import com.sourceplusplus.protocol.advice.ArtifactAdvice
import com.sourceplusplus.protocol.artifact.ArtifactQualifiedName
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class SourceMentor : CoroutineVerticle() {

    companion object {
        private val log = LoggerFactory.getLogger(SourceMentor::class.java)
    }

    private val setupLock = AtomicBoolean()
    private val jobList = mutableListOf<MentorJob>()
    private val taskQueue = PriorityQueue<MentorTask>()

    fun setup() {
        if (!setupLock.compareAndSet(false, true)) {
            log.info("Setting up SourceMentor")
        } else {
            throw IllegalStateException("Already setup")
        }
    }

    fun addJob(job: MentorJob) {
        jobList.add(job)
    }

    fun getAllMethodAdvice(methodQualifiedName: ArtifactQualifiedName): List<ArtifactAdvice> {
        return emptyList() //todo: this
    }
}
