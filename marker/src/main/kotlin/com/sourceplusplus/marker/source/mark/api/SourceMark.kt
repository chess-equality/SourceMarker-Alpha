package com.sourceplusplus.marker.source.mark.api

import com.intellij.openapi.util.Key
import com.sourceplusplus.marker.SourceFileMarker
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEvent
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventCode
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventListener
import com.sourceplusplus.marker.source.mark.api.key.SourceKey

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
interface SourceMark {

    enum class Type {
        GUTTER
    }

    companion object {
        @JvmField
        val KEY = Key.create<SourceMark>("sm.SourceMark")
    }

    val isClassMark: Boolean
    val isMethodMark: Boolean
    val moduleName: String
    val artifactQualifiedName: String
    val sourceFileMarker: SourceFileMarker
    val valid: Boolean

    fun apply() {
        triggerEvent(SourceMarkEvent(this, SourceMarkEventCode.MARK_ADDED))
    }

    fun dispose() {
        dispose(true)
    }

    fun dispose(removeFromMarker: Boolean = true)
    fun <T> getUserData(key: SourceKey<T>): T?
    fun <T> putUserData(key: SourceKey<T>, value: T?)

    fun getEventListeners(): List<SourceMarkEventListener>
    fun addEventListener(listener: SourceMarkEventListener)
    fun triggerEvent(event: SourceMarkEvent) {
        getEventListeners().forEach { it.handleEvent(event) }
    }
}