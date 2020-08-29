package com.sourceplusplus.marker.source.mark.api

import com.intellij.openapi.roots.ProjectRootManager
import com.sourceplusplus.marker.SourceFileMarker
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventListener
import com.sourceplusplus.marker.source.mark.api.key.SourceKey
import org.jetbrains.uast.UClass

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
abstract class ClassSourceMark(
        override val sourceFileMarker: SourceFileMarker,
        internal open val psiClass: UClass,
        override val artifactQualifiedName: String = psiClass.qualifiedName!!
) : SourceMark {

    override val isClassMark: Boolean = true
    override val isMethodMark: Boolean = false
    override val valid: Boolean; get() = psiClass.isPsiValid && artifactQualifiedName == psiClass.qualifiedName!!
    override val moduleName: String
        get() = ProjectRootManager.getInstance(sourceFileMarker.project).fileIndex
                .getModuleForFile(psiClass.containingFile.virtualFile)!!.name

    private val userData = HashMap<Any, Any>()
    override fun <T> getUserData(key: SourceKey<T>): T? = userData[key] as T?
    override fun <T> putUserData(key: SourceKey<T>, value: T?) {
        if (value != null) {
            userData.put(key, value)
        } else {
            userData.remove(key)
        }
    }

    fun getPsiClass(): UClass {
        return psiClass
    }

    private val eventListeners = ArrayList<SourceMarkEventListener>()
    override fun getEventListeners(): List<SourceMarkEventListener> = eventListeners.toList()
    override fun addEventListener(listener: SourceMarkEventListener) {
        eventListeners += listener
    }

    /**
     * {@inheritDoc}
     */
    override fun toString(): String = "${javaClass.simpleName}: $artifactQualifiedName"

    /**
     * {@inheritDoc}
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClassSourceMark) return false
        if (artifactQualifiedName != other.artifactQualifiedName) return false
        return true
    }

    /**
     * {@inheritDoc}
     */
    override fun hashCode(): Int {
        return artifactQualifiedName.hashCode()
    }
}