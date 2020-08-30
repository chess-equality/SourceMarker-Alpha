package com.sourceplusplus.marker

import com.intellij.lang.Language
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.ex.util.EditorUtil
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.util.PsiUtil
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.members.GrMethod
import org.jetbrains.uast.UMethod
import java.awt.Point

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
class MarkerUtils private constructor() {
    companion object {

        @JvmStatic
        fun getNameIdentifier(nameIdentifierOwner: PsiNameIdentifierOwner): PsiElement? {
            return when {
                nameIdentifierOwner.language === Language.findLanguageByID("kotlin") -> {
                    when (nameIdentifierOwner) {
                        is KtNamedFunction -> nameIdentifierOwner.nameIdentifier
                        else -> (nameIdentifierOwner.navigationElement as KtNamedFunction).nameIdentifier
                    }
                }
                nameIdentifierOwner.language === Language.findLanguageByID("Groovy") -> {
                    (nameIdentifierOwner.navigationElement as GrMethod).nameIdentifierGroovy //todo: why can't be null?
                }
                else -> nameIdentifierOwner.nameIdentifier
            }
        }

        @JvmStatic
        fun getQualifiedClassName(qualifiedName: String): String {
            var withoutArgs = qualifiedName.substring(0, qualifiedName.indexOf("("))
            return if (withoutArgs.contains("<")) {
                withoutArgs = withoutArgs.substring(0, withoutArgs.indexOf("<"))
                withoutArgs.substring(withoutArgs.lastIndexOf("?") + 1, withoutArgs.lastIndexOf("."))
            } else {
                withoutArgs.substring(withoutArgs.lastIndexOf("?") + 1, withoutArgs.lastIndexOf("."))
            }
        }

        @JvmStatic
        fun getFullyQualifiedName(method: UMethod): String {
            //todo: PsiUtil.getMemberQualifiedName(method)!!
            return "${method.containingClass!!.qualifiedName}.${getQualifiedName(method)}"
        }

        @JvmStatic
        fun getQualifiedName(method: UMethod): String {
            val methodName = method.nameIdentifier!!.text
            var methodParams = ""
            method.parameterList.parameters.forEach {
                if (methodParams.isNotEmpty()) {
                    methodParams += ","
                }
                val qualifiedType = PsiUtil.resolveClassInType(it.type)
                val arrayDimensions = getArrayDimensions(it.type.toString())
                if (qualifiedType != null) {
                    methodParams += if (qualifiedType.containingClass != null) {
                        qualifiedType.containingClass!!.qualifiedName + '$' + qualifiedType.name
                    } else {
                        qualifiedType.qualifiedName
                    }
                    for (i in 0 until arrayDimensions) {
                        methodParams += "[]"
                    }
                } else {
                    methodParams += it.typeElement!!.text
                }
            }
            return "$methodName($methodParams)"
        }

        @JvmStatic
        fun convertPointToLineNumber(project: Project, p: Point): Int {
            val myEditor = FileEditorManager.getInstance(project).selectedTextEditor
            val document = myEditor!!.document
            val line = EditorUtil.yPositionToLogicalLine(myEditor, p)
            if (!isValidLine(document, line)) return -1
            val startOffset = document.getLineStartOffset(line)
            val region = myEditor.foldingModel.getCollapsedRegionAtOffset(startOffset)
            return if (region != null) {
                document.getLineNumber(region.endOffset)
            } else line
        }

        private fun isValidLine(document: Document, line: Int): Boolean {
            if (line < 0) return false
            val lineCount = document.lineCount
            return if (lineCount == 0) line == 0 else line < lineCount
        }

        private fun getArrayDimensions(s: String): Int {
            var arrayDimensions = 0
            for (element in s) {
                if (element == '[') {
                    arrayDimensions++
                }
            }
            return arrayDimensions
        }
    }
}