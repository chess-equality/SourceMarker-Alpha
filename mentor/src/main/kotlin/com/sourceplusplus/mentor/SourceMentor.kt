package com.sourceplusplus.mentor

import com.sourceplusplus.protocol.advice.method.MethodAdvice
import com.sourceplusplus.protocol.artifact.ArtifactQualifiedName
import org.slf4j.LoggerFactory
import java.util.*

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class SourceMentor {

    companion object {
        private val log = LoggerFactory.getLogger(SourceMentor::class.java)
    }

    val queue = PriorityQueue<MentorTask>()

    fun getAllMethodAdvice(methodQualifiedName: ArtifactQualifiedName): List<MethodAdvice> {
        TODO()
    }
}
