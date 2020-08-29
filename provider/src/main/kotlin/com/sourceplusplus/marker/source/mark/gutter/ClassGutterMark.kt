package com.sourceplusplus.marker.source.mark.gutter

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiInvalidElementAccessException
import com.sourceplusplus.marker.SourceFileMarker
import com.sourceplusplus.marker.plugin.SourceMarkerPlugin
import com.sourceplusplus.marker.source.mark.api.ClassSourceMark
import com.sourceplusplus.marker.source.mark.gutter.component.api.GutterMarkComponent
import com.sourceplusplus.marker.source.mark.gutter.config.GutterMarkConfiguration
import org.jetbrains.uast.UClass
import java.util.concurrent.atomic.AtomicBoolean

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
open class ClassGutterMark(
        override val sourceFileMarker: SourceFileMarker,
        override val psiClass: UClass
) : ClassSourceMark(sourceFileMarker, psiClass), GutterMark {

    final override val configuration: GutterMarkConfiguration = SourceMarkerPlugin.configuration.defaultGutterMarkConfiguration
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
    override fun isVisible(): Boolean {
        return visible.get()
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
            val document = psiClass.nameIdentifier!!.containingFile.viewProvider.document
            return document!!.getLineNumber(psiClass.nameIdentifier!!.textRange.startOffset)
        }

    override val viewProviderBound: Boolean
        get() = try {
            psiClass.nameIdentifier!!.containingFile.viewProvider.document
            true
        } catch (ignore: PsiInvalidElementAccessException) {
            false
        }
}