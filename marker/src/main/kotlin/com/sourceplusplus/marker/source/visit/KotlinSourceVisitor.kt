package com.sourceplusplus.marker.source.visit

import com.sourceplusplus.marker.MarkerUtils
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.toUElement

/**
 * Visits Kotlin source files for mark-able elements.
 * <p>
 * Currently supports: methods
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
open class KotlinSourceVisitor(
        override val visitedMethods: MutableList<String> = ArrayList()
) : KtTreeVisitorVoid(), SourceVisitor {

    override fun visitNamedFunction(function: KtNamedFunction) {
        if (function.fqName != null) {
            val uMethod = function.toUElement() as UMethod
            visitedMethods.add(MarkerUtils.getFullyQualifiedName(uMethod))
        }
        super.visitNamedFunction(function)
    }
}