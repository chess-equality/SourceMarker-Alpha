package com.sourceplusplus.mapper.vcs.git

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaRecursiveElementVisitor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.sourceplusplus.marker.MarkerUtils
import jp.ac.titech.c.se.stein.core.Context
import jp.ac.titech.c.se.stein.core.EntrySet
import jp.ac.titech.c.se.stein.core.EntrySet.Entry
import jp.ac.titech.c.se.stein.core.EntrySet.EntryList
import jp.ac.titech.c.se.stein.core.RepositoryRewriter
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.plugins.groovy.GroovyFileType
import org.jetbrains.uast.UFile
import org.jetbrains.uast.toUElement
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets

/**
 * Based off FinerGit
 */
class GitRepositoryMapper(private val project: Project) : RepositoryRewriter() {
    //todo: ability to add to existing mapped repo

    companion object {
        private val log = LoggerFactory.getLogger(GitRepositoryMapper::class.java)
    }

    init {
        isPathSensitive = true
    }

    override fun rewriteEntry(entry: Entry, c: Context): EntrySet {
        if (entry.isTree) {
            return super.rewriteEntry(entry, c)
        }

        val fileType = FileTypeRegistry.getInstance().getFileTypeByFileName(entry.name)
        if (fileType !is JavaFileType && fileType !is GroovyFileType && fileType !is KotlinFileType) {
            return Entry.EMPTY //unsupported file type
        }

        val psiFile = PsiFileFactory.getInstance(project).createFileFromText(
            entry.name, fileType,
            String(source.readBlob(entry.id, c), StandardCharsets.UTF_8)
        )

        val result = EntryList()
        val uFile = psiFile.toUElement() as UFile
        uFile.classes.forEach { uClass ->
            uClass.methods.forEach {
                val tokenStr = StringBuilder()
                it.javaPsi.accept(object : JavaRecursiveElementVisitor() {
                    override fun visitElement(element: PsiElement) {
                        if (element is LeafPsiElement && element.text.isNotBlank()) {
                            tokenStr.append(element.text).append("\n")
                        }
                        super.visitElement(element)
                    }
                })

                val newId = target.writeBlob(tokenStr.toString().toByteArray(StandardCharsets.UTF_8), c)
                val name =
                    "${MarkerUtils.getFullyQualifiedName(it)}.m${uFile.lang.associatedFileType!!.defaultExtension}"
                result.add(Entry(entry.mode, name, newId, entry.directory))
            }
        }

        return result
    }
}
//git log --all --full-history -- <path-to-file>