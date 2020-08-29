package com.sourceplusplus.marker.source.visit

import com.intellij.psi.JavaRecursiveElementVisitor
import com.intellij.psi.PsiMethod
import com.sourceplusplus.marker.MarkerUtils
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.toUElement

/**
 * Visits Java source files for mark-able elements.
 * <p>
 * Currently supports: methods
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
open class JavaSourceVisitor(
        override val visitedMethods: MutableList<String> = ArrayList()
) : JavaRecursiveElementVisitor(), SourceVisitor {

    override fun visitMethod(method: PsiMethod) {
        val uMethod = method.toUElement() as UMethod
        visitedMethods.add(MarkerUtils.getFullyQualifiedName(uMethod))
        super.visitMethod(method)
    }
}