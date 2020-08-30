package com.sourceplusplus.marker

import com.google.common.collect.ImmutableList
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.psi.*
import com.sourceplusplus.marker.plugin.SourceMarkerPlugin
import com.sourceplusplus.marker.source.mark.api.MethodSourceMark
import com.sourceplusplus.marker.source.mark.api.SourceMark
import com.sourceplusplus.marker.source.mark.api.SourceMarkProvider
import com.sourceplusplus.marker.source.mark.gutter.MethodGutterMark
import com.sourceplusplus.marker.source.visit.GroovySourceVisitor
import com.sourceplusplus.marker.source.visit.JavaSourceVisitor
import com.sourceplusplus.marker.source.visit.KotlinSourceVisitor
import com.sourceplusplus.marker.source.visit.SourceVisitor
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.plugins.groovy.lang.psi.GroovyElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UMethod
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.ConcurrentHashMap


/**
 * Used to mark a source code file with Source++ artifact marks.
 * Source++ artifact marks can be used to subscribe to and collect source code runtime information.
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
open class SourceFileMarker(val psiFile: PsiFile) : SourceMarkProvider {

    companion object {
        val KEY = Key.create<SourceFileMarker>("sm.SourceFileMarker")
        private val log = LoggerFactory.getLogger(SourceFileMarker::class.java)

        @JvmStatic
        fun isFileSupported(psiFile: PsiFile): Boolean {
            return try {
                when (psiFile) {
                    is GroovyFile -> true
                    is PsiJavaFile -> true
                    is KtFile -> true
                    else -> false
                }
            } catch (t: Throwable) {
                false
            }
        }
    }

    private val rejectedSourceMarks: MutableSet<SourceMark> = Collections.newSetFromMap(ConcurrentHashMap())
    private val sourceMarks: MutableSet<SourceMark> = Collections.newSetFromMap(ConcurrentHashMap())
    val project: Project = psiFile.project

    /**
     * Gets the [SourceMark]s recognized but rejected in the current source code file.
     *
     * @return a list of the rejected [SourceMark]s
     */
    open fun getRejectedSourceMarks(): List<SourceMark> {
        rejectedSourceMarks.removeIf {
            !it.valid || SourceMarkerPlugin.configuration.sourceMarkFilter.test(it)
        }
        return ImmutableList.copyOf(rejectedSourceMarks)
    }

    /**
     * Gets the [SourceMark]s recognized in the current source code file.
     *
     * @return a list of the [SourceMark]s
     */
    open fun getSourceMarks(): List<SourceMark> {
        return ImmutableList.copyOf(sourceMarks)
    }

    open fun refresh() {
        if (!psiFile.project.isDisposed) {
            DaemonCodeAnalyzer.getInstance(psiFile.project).restart(psiFile)
        }
    }

    open fun clearSourceMarks() {
        rejectedSourceMarks.clear()
        val removed = sourceMarks.removeIf {
            it.dispose(false)
            true
        }
        if (removed) refresh()
    }

    open fun removeIfInvalid(sourceMark: SourceMark): Boolean {
        var removedMark = false
        if (!sourceMark.valid || !SourceMarkerPlugin.configuration.sourceMarkFilter.test(sourceMark)) {
            check(removeSourceMark(sourceMark))
            removedMark = true
        }
        if (removedMark) refresh()
        return removedMark
    }

    open fun removeInvalidSourceMarks(): Boolean {
        var sourceVisitor: SourceVisitor
        when (psiFile) {
            is GroovyFile -> psiFile.accept(GroovySourceVisitor().also { sourceVisitor = it } as GroovyElementVisitor)
            is PsiJavaFile -> psiFile.accept(JavaSourceVisitor().also { sourceVisitor = it } as PsiElementVisitor)
            is KtFile -> psiFile.accept(KotlinSourceVisitor().also { sourceVisitor = it } as PsiElementVisitor)
            else -> throw IllegalStateException("Unsupported file: $psiFile")
        }

        var removedMark = false
        sourceMarks.forEach {
            if (!it.valid || !sourceVisitor.visitedMethods.contains(it.artifactQualifiedName)
                || !SourceMarkerPlugin.configuration.sourceMarkFilter.test(it)
            ) {
                check(removeSourceMark(it))
                removedMark = true
            }
        }
        if (removedMark) refresh()
        return removedMark
    }

    @JvmOverloads
    open fun removeSourceMark(
        sourceMark: SourceMark,
        autoRefresh: Boolean = false,
        autoDispose: Boolean = true
    ): Boolean {
        log.trace("Removing source mark for artifact: " + sourceMark.artifactQualifiedName)
        return if (sourceMarks.remove(sourceMark)) {
            if (autoDispose) sourceMark.dispose(false)
            if (autoRefresh) refresh()
            log.trace("Removed source mark for artifact: " + sourceMark.artifactQualifiedName)
            true
        } else false
    }

    @JvmOverloads
    open fun applySourceMark(sourceMark: SourceMark, autoRefresh: Boolean = false, autoApply: Boolean = true): Boolean {
        if (SourceMarkerPlugin.configuration.sourceMarkFilter.test(sourceMark)) {
            log.trace("Applying source mark for artifact: " + sourceMark.artifactQualifiedName)
            if (sourceMarks.add(sourceMark)) {
                if (autoApply) sourceMark.apply()
                if (autoRefresh) refresh()
                log.trace("Applied source mark for artifact: " + sourceMark.artifactQualifiedName)
                return true
            }
        } else {
            rejectedSourceMarks.add(sourceMark)
        }
        return false
    }

    open fun getSourceMark(artifactQualifiedName: String?): SourceMark? {
        return sourceMarks.find { it.artifactQualifiedName == artifactQualifiedName }
    }

    open fun getMethodSourceMark(psiMethod: PsiElement): MethodSourceMark? {
        return sourceMarks.find {
            it is MethodSourceMark && it.psiMethod.sourcePsi === psiMethod
        } as MethodSourceMark?
    }

    /**
     * {@inheritDoc}
     */
    override fun createSourceMark(psiMethod: UMethod, type: SourceMark.Type): SourceMark {
        when (type) {
            SourceMark.Type.GUTTER -> {
                return MethodGutterMark(this, psiMethod)
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun createSourceMark(psiClass: UClass, type: SourceMark.Type): SourceMark {
        TODO("Not yet implemented")
    }

    open fun getClassQualifiedNames(): List<String> {
        return when (psiFile) {
            is PsiClassOwner -> psiFile.classes.map { it.qualifiedName!! }.toList()
            else -> throw IllegalStateException("Unsupported file: $psiFile")
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SourceFileMarker
        if (psiFile != other.psiFile) return false
        return true
    }

    /**
     * {@inheritDoc}
     */
    override fun hashCode(): Int {
        return psiFile.hashCode()
    }

    /**
     * {@inheritDoc}
     */
    override fun toString(): String {
        return "SourceFileMarker:${psiFile.name}"
    }
}