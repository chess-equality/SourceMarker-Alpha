package com.sourceplusplus.marker.source.mark.api

import com.intellij.openapi.roots.ProjectRootManager
import com.sourceplusplus.marker.MarkerUtils
import com.sourceplusplus.marker.SourceFileMarker
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEvent
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventCode
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventListener
import com.sourceplusplus.marker.source.mark.api.key.SourceKey
import org.jetbrains.uast.UMethod

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
abstract class MethodSourceMark(
        override val sourceFileMarker: SourceFileMarker,
        internal open var psiMethod: UMethod,
        override var artifactQualifiedName: String = MarkerUtils.getFullyQualifiedName(psiMethod)
) : SourceMark {

    override val isClassMark: Boolean = false
    override val isMethodMark: Boolean = true
    override val valid: Boolean; get() = psiMethod.isPsiValid && artifactQualifiedName == MarkerUtils.getFullyQualifiedName(psiMethod)
    override val moduleName: String
        get() = ProjectRootManager.getInstance(sourceFileMarker.project).fileIndex
                .getModuleForFile(psiMethod.containingFile.virtualFile)!!.name

    private val userData = HashMap<Any, Any>()
    override fun <T> getUserData(key: SourceKey<T>): T? = userData[key] as T?
    override fun <T> putUserData(key: SourceKey<T>, value: T?) {
        if (value != null) {
            userData.put(key, value)
        } else {
            userData.remove(key)
        }
    }

    fun getPsiMethod(): UMethod {
        return psiMethod
    }

    fun updatePsiMethod(psiMethod: UMethod): Boolean {
        this.psiMethod = psiMethod
        val newArtifactQualifiedName = MarkerUtils.getFullyQualifiedName(psiMethod)
        if (artifactQualifiedName != newArtifactQualifiedName) {
            check(sourceFileMarker.removeSourceMark(this, autoRefresh = false, autoDispose = false))
            val oldArtifactQualifiedName = artifactQualifiedName
            artifactQualifiedName = newArtifactQualifiedName
            return if (sourceFileMarker.applySourceMark(this, autoRefresh = false, autoApply = false)) {
                triggerEvent(SourceMarkEvent(this, SourceMarkEventCode.NAME_CHANGED, oldArtifactQualifiedName))
                true
            } else false
        }
        return true
    }

    fun isStaticMethod(): Boolean {
        return psiMethod.isStatic
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
        //todo: SourceFileMarker bases off psiFile, this class might need to base off psiMethod
        if (this === other) return true
        if (other !is MethodSourceMark) return false
        if (artifactQualifiedName != other.artifactQualifiedName) return false
        return true
    }

    /**
     * {@inheritDoc}
     */
    override fun hashCode(): Int {
        //todo: SourceFileMarker bases off psiFile, this class might need to base off psiMethod
        return artifactQualifiedName.hashCode()
    }
}