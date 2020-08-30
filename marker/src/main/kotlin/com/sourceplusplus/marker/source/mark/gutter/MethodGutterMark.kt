package com.sourceplusplus.marker.source.mark.gutter

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiInvalidElementAccessException
import com.intellij.psi.PsiNameIdentifierOwner
import com.sourceplusplus.marker.MarkerUtils
import com.sourceplusplus.marker.SourceFileMarker
import com.sourceplusplus.marker.source.mark.api.MethodSourceMark
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEvent
import com.sourceplusplus.marker.source.mark.gutter.component.api.GutterMarkComponent
import com.sourceplusplus.marker.source.mark.gutter.config.GutterMarkConfiguration
import com.sourceplusplus.marker.source.mark.gutter.event.GutterMarkEventCode
import org.jetbrains.uast.UMethod
import java.util.concurrent.atomic.AtomicBoolean
import com.sourceplusplus.marker.plugin.SourceMarkerPlugin.configuration as pluginConfiguration

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
open class MethodGutterMark(
        override val sourceFileMarker: SourceFileMarker,
        override var psiMethod: UMethod
) : MethodSourceMark(sourceFileMarker, psiMethod), GutterMark {

    final override val configuration: GutterMarkConfiguration = pluginConfiguration.defaultGutterMarkConfiguration.copy()
    final override lateinit var gutterMarkComponent: GutterMarkComponent
    private var visible: AtomicBoolean = AtomicBoolean()
    override var editor: Editor? = null
    override var visiblePopup: Disposable? = null

    /**
     * {@inheritDoc}
     */
    override fun apply() {
        gutterMarkComponent = configuration.componentProvider.getComponent(this)
        super<GutterMark>.apply()
    }

    /**
     * {@inheritDoc}
     */
    override fun dispose(removeFromMarker: Boolean) {
        MarkerUtils.getNameIdentifier(psiMethod as PsiNameIdentifierOwner)!!.putUserData(GutterMark.KEY, null)
        super<GutterMark>.dispose(removeFromMarker)
    }

    /**
     * {@inheritDoc}
     */
    override fun isVisible(): Boolean {
        return visible.get()
    }

    fun setVisible(visible: Boolean) {
        val previousVisibility = this.visible.getAndSet(visible)
        if (visible && !previousVisibility) {
            triggerEvent(SourceMarkEvent(this, GutterMarkEventCode.GUTTER_MARK_VISIBLE))
        } else if (!visible && previousVisibility) {
            triggerEvent(SourceMarkEvent(this, GutterMarkEventCode.GUTTER_MARK_HIDDEN))
        }
    }

    /**
     * Line number of the gutter mark.
     * One above the method name identifier.
     * First line for class (maybe? might want to make that for package level stats in the future)
     *
     * @return gutter mark line number
     */
    override val lineNumber: Int
        get() {
            val document = psiMethod.nameIdentifier!!.containingFile.viewProvider.document
            return document!!.getLineNumber(psiMethod.nameIdentifier!!.textRange.startOffset)
        }

    override val viewProviderBound: Boolean
        get() = try {
            psiMethod.nameIdentifier!!.containingFile.viewProvider.document
            true
        } catch (ignore: PsiInvalidElementAccessException) {
            false
        }
}