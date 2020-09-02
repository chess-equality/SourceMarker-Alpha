package com.sourceplusplus.marker.source.visit

import com.sourceplusplus.marker.MarkerUtils
import org.jetbrains.plugins.groovy.lang.psi.GroovyRecursiveElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.members.GrMethod
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.toUElement

/**
 * Visits Groovy source files for mark-able elements.
 * <p>
 * Currently supports: methods
 *
 * @version 0.2.2
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
open class GroovySourceVisitor(
        override val visitedMethods: MutableList<String> = ArrayList()
) : GroovyRecursiveElementVisitor(), SourceVisitor {

    override fun visitMethod(method: GrMethod) {
        val uMethod = method.toUElement() as UMethod
        visitedMethods.add(MarkerUtils.getFullyQualifiedName(uMethod))
        super.visitMethod(method)
    }
}