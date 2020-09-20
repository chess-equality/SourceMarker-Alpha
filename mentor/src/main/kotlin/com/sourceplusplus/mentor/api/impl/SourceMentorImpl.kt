package com.sourceplusplus.mentor.api.impl

import com.sourceplusplus.mentor.api.SourceMentor
import com.sourceplusplus.protocol.advice.method.MethodAdvice
import com.sourceplusplus.protocol.artifact.ArtifactQualifiedName
import org.slf4j.LoggerFactory

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class SourceMentorImpl : SourceMentor {

    companion object {
        private val log = LoggerFactory.getLogger(SourceMentorImpl::class.java)
    }

    private var timezone: Int = 0

    override fun getAllMethodAdvice(methodQualifiedName: ArtifactQualifiedName): List<MethodAdvice> {
        return listOf()
    }
}
