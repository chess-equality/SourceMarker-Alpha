package com.sourceplusplus.sourcemarker.psi

import com.intellij.openapi.application.ApplicationManager
import com.sourceplusplus.marker.source.mark.api.MethodSourceMark
import io.vertx.core.Future
import io.vertx.core.Promise
import org.jetbrains.uast.expressions.UInjectionHost
import org.jetbrains.uast.java.JavaUQualifiedReferenceExpression
import java.util.*

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
class EndpointNameDetector {

    private val requestMappingAnnotation = "org.springframework.web.bind.annotation.RequestMapping"

    //todo: this code is specific to RequestMapping, also specific to Java
    fun determineEndpointName(sourceMark: MethodSourceMark): Future<Optional<String>> {
        val promise = Promise.promise<Optional<String>>()
        ApplicationManager.getApplication().runReadAction {
            val requestMappingAnnotation = sourceMark.getPsiMethod().findAnnotation(requestMappingAnnotation)
            if (requestMappingAnnotation != null) {
                val value = (requestMappingAnnotation.findAttributeValue("value") as UInjectionHost).evaluateToString()
                val method = (requestMappingAnnotation.findAttributeValue("method")
                        as JavaUQualifiedReferenceExpression).selector
                promise.complete(Optional.of("{$method}$value"))
            } else {
                promise.complete(Optional.empty())
            }
        }
        return promise.future()
    }
}
