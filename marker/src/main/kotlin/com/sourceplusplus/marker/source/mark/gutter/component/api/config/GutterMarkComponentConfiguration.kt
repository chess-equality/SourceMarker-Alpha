package com.sourceplusplus.marker.source.mark.gutter.component.api.config

import com.intellij.openapi.editor.Editor
import java.awt.Dimension

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
open class GutterMarkComponentConfiguration {

    var useHeavyPopup = true
    var hideOnMouseMotion = false
    var hideOnScroll = true
    var showAboveClass: Boolean = true //todo: impl
    var showAboveMethod: Boolean = true //todo: impl
    var componentSizeEvaluator: ComponentSizeEvaluator = ComponentSizeEvaluator()
    internal var addedMouseMotionListener: Boolean = false
    internal var addedScrollListener: Boolean = false

    open fun copy(): GutterMarkComponentConfiguration {
        val copy = GutterMarkComponentConfiguration()
        copy.useHeavyPopup = useHeavyPopup
        copy.hideOnMouseMotion = hideOnMouseMotion
        copy.hideOnScroll = hideOnScroll
        copy.showAboveClass = showAboveClass
        copy.showAboveMethod = showAboveMethod
        copy.componentSizeEvaluator = componentSizeEvaluator
        return copy
    }
}

open class ComponentSizeEvaluator {
    open fun getDynamicSize(editor: Editor, configuration: GutterMarkComponentConfiguration): Dimension? {
        return null
    }
}