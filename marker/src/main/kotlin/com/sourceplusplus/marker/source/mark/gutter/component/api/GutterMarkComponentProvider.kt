package com.sourceplusplus.marker.source.mark.gutter.component.api

import com.sourceplusplus.marker.source.mark.gutter.GutterMark
import com.sourceplusplus.marker.source.mark.gutter.component.api.config.GutterMarkComponentConfiguration

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
abstract class GutterMarkComponentProvider {

    abstract val defaultConfiguration: GutterMarkComponentConfiguration

    abstract fun getComponent(gutterMark: GutterMark): GutterMarkComponent
    abstract fun disposeComponent(gutterMark: GutterMark)
}