package com.sourceplusplus.marker.plugin

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.openapi.editor.markup.GutterIconRenderer.Alignment.CENTER
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.sourceplusplus.marker.source.mark.api.SourceMark
import com.sourceplusplus.marker.source.mark.gutter.GutterMark
import com.sourceplusplus.marker.source.mark.gutter.MethodGutterMark
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.members.GrMethod
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.toUElement

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
abstract class SourceLineMarkerProvider : LineMarkerProviderDescriptor() {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
        if (!SourceMarkerPlugin.enabled) {
            return null
        }

        val parent = element.parent
        if ((parent is PsiMethod && element === parent.nameIdentifier)
                || (parent is GrMethod && element === parent.nameIdentifierGroovy)
                || (parent is KtNamedFunction && element === parent.nameIdentifier)) {
            val fileMarker = SourceMarkerPlugin.getSourceFileMarker(element.containingFile) ?: return null
            var gutterMark = element.getUserData(GutterMark.KEY) as MethodGutterMark?
            if (gutterMark == null) {
                gutterMark = fileMarker.getMethodSourceMark(element.parent) as MethodGutterMark?
                if (gutterMark != null) {
                    if (gutterMark.updatePsiMethod(element.parent.toUElement() as UMethod)) {
                        element.putUserData(GutterMark.KEY, gutterMark)
                    } else {
                        gutterMark = null
                    }
                }
            }

            if (gutterMark == null) {
                gutterMark = fileMarker.createSourceMark(
                        element.parent.toUElement() as UMethod, SourceMark.Type.GUTTER) as MethodGutterMark
                if (fileMarker.applySourceMark(gutterMark)) {
                    element.putUserData(GutterMark.KEY, gutterMark)

                    if (gutterMark.configuration.icon != null) {
                        gutterMark.setVisible(true)

                        var navigationHandler: GutterIconNavigationHandler<PsiElement>? = null
                        if (gutterMark.configuration.activateOnMouseClick) {
                            navigationHandler = GutterIconNavigationHandler<PsiElement> { _, elt ->
                                elt!!.getUserData(GutterMark.KEY)!!.displayPopup()
                            }
                        }
                        return LineMarkerInfo(element, element.textRange, gutterMark.configuration.icon,
                                null, navigationHandler, CENTER)
                    } else {
                        gutterMark.setVisible(false)
                    }
                }
            } else {
                if (fileMarker.removeIfInvalid(gutterMark)) {
                    element.putUserData(GutterMark.KEY, null)
                } else if (gutterMark.configuration.icon != null) {
                    gutterMark.setVisible(true)

                    var navigationHandler: GutterIconNavigationHandler<PsiElement>? = null
                    if (gutterMark.configuration.activateOnMouseClick) {
                        navigationHandler = GutterIconNavigationHandler<PsiElement> { _, elt ->
                            elt!!.getUserData(GutterMark.KEY)!!.displayPopup()
                        }
                    }
                    return LineMarkerInfo(element, element.textRange, gutterMark.configuration.icon,
                            null, navigationHandler, CENTER)
                } else {
                    gutterMark.setVisible(false)
                }
            }
        }
        //todo: class mark

        return null
    }

    override fun collectSlowLineMarkers(elements: MutableList<out PsiElement>, result: MutableCollection<in LineMarkerInfo<*>>) {
        if (!SourceMarkerPlugin.enabled) {
            return
        }

        elements.stream().map { it.containingFile }.distinct().forEach {
            SourceMarkerPlugin.getSourceFileMarker(it)?.removeInvalidSourceMarks()
        }
    }

    class GroovyDescriptor : SourceLineMarkerProvider() {
        override fun getName(): String {
            return "Groovy source line markers"
        }
    }

    class JavaDescriptor : SourceLineMarkerProvider() {
        override fun getName(): String {
            return "Java source line markers"
        }
    }

    class KotlinDescriptor : SourceLineMarkerProvider() {
        override fun getName(): String {
            return "Kotlin source line markers"
        }
    }
}