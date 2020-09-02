package com.sourceplusplus.marker.source.mark.api.component.api

import com.sourceplusplus.marker.source.mark.api.SourceMark
import com.sourceplusplus.marker.source.mark.api.component.api.config.SourceMarkComponentConfiguration

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
abstract class SourceMarkComponentProvider {

    abstract val defaultConfiguration: SourceMarkComponentConfiguration

    abstract fun getComponent(sourceMark: SourceMark): SourceMarkComponent
    abstract fun disposeComponent(sourceMark: SourceMark)
}