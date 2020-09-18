package com.sourceplusplus.sourcemarker.psi

import com.sourceplusplus.marker.source.mark.api.MethodSourceMark
import org.jetbrains.uast.expressions.UInjectionHost
import org.jetbrains.uast.java.JavaUQualifiedReferenceExpression

class EndpointNameDetector {

    private val requestMappingAnnotation = "org.springframework.web.bind.annotation.RequestMapping"

    //todo: this code is specific to RequestMapping, also specific to Java
    fun determineEndpointName(sourceMark: MethodSourceMark): String? {
        val requestMappingAnnotation =
            sourceMark.getPsiMethod().findAnnotation(requestMappingAnnotation)
        if (requestMappingAnnotation != null) {
            val value = (requestMappingAnnotation.findAttributeValue("value") as UInjectionHost).evaluateToString()
            val method =
                (requestMappingAnnotation.findAttributeValue("method") as JavaUQualifiedReferenceExpression).selector
            return "{${method}}$value"
        }
        return null
    }
}