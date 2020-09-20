package com.sourceplusplus.mentor.api

import com.sourceplusplus.protocol.advice.method.MethodAdvice
import com.sourceplusplus.protocol.artifact.ArtifactQualifiedName

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
interface SourceMentor {

    fun getAllMethodAdvice(methodQualifiedName: ArtifactQualifiedName): List<MethodAdvice>
}
