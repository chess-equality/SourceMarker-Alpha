package com.sourceplusplus.marker.source.mark.gutter

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.VisibleAreaEvent
import com.intellij.openapi.editor.event.VisibleAreaListener
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.JBPopupListener
import com.intellij.openapi.ui.popup.LightweightWindowEvent
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Key
import com.intellij.ui.BalloonImpl
import com.intellij.ui.JBColor
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.ui.JBUI
import com.sourceplusplus.marker.source.mark.api.ClassSourceMark
import com.sourceplusplus.marker.source.mark.api.MethodSourceMark
import com.sourceplusplus.marker.source.mark.api.SourceMark
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEvent
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventCode
import com.sourceplusplus.marker.source.mark.gutter.component.api.GutterMarkComponent
import com.sourceplusplus.marker.source.mark.gutter.config.GutterMarkConfiguration
import org.slf4j.LoggerFactory
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.SwingUtilities
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
interface GutterMark : SourceMark, JBPopupListener, MouseMotionListener, VisibleAreaListener {

    companion object {
        val KEY = Key.create<GutterMark>("sm.SourceMark")

        private val log = LoggerFactory.getLogger(GutterMark::class.java)
        private val buildingPopup = AtomicBoolean()
        private var openedMarks: MutableList<GutterMark> = ArrayList()

        @JvmStatic
        fun closeOpenPopups() {
            openedMarks.toList().forEach {
                it.closePopup()
            }
        }
    }

    val project: Project; get() = sourceFileMarker.project
    val lineNumber: Int
    val viewProviderBound: Boolean
    val configuration: GutterMarkConfiguration
    val gutterMarkComponent: GutterMarkComponent
    var editor: Editor?
    var visiblePopup: Disposable?

    fun isVisible(): Boolean

    fun closePopup() {
        if (openedMarks.remove(this)) {
            log.debug("Closing popup")
            try {
                if (gutterMarkComponent.configuration.addedMouseMotionListener) {
                    editor?.contentComponent?.removeMouseMotionListener(this)
                    gutterMarkComponent.configuration.addedMouseMotionListener = false
                }
            } catch (ignore: Throwable) {
            }
            try {
                if (gutterMarkComponent.configuration.addedScrollListener) {
                    editor?.scrollingModel?.removeVisibleAreaListener(this)
                    gutterMarkComponent.configuration.addedScrollListener = false
                }
            } catch (ignore: Throwable) {
            }
            editor = null
            if (visiblePopup != null) {
                Disposer.dispose(visiblePopup!!)
            }
            visiblePopup = null
        }
    }

    fun displayPopup() {
        displayPopup(FileEditorManager.getInstance(sourceFileMarker.project).selectedTextEditor!!)
    }

    fun displayPopup(editor: Editor = FileEditorManager.getInstance(sourceFileMarker.project).selectedTextEditor!!) {
        if (visiblePopup != null || buildingPopup.getAndSet(true)) {
            log.trace("Ignore display popup")
            return
        } else {
            log.debug("Displaying popup")

            //todo: only close marks which are necessary to close
            closeOpenPopups()
            triggerDisplay(editor)
        }
    }

    private fun triggerDisplay(editor: Editor) {
        this.editor = editor

        SwingUtilities.invokeLater {
            val displayPoint = editor.visualPositionToXY(editor.offsetToVisualPosition(
                    editor.document.getLineStartOffset(lineNumber)))

            //            if (!configuration.usingJcefBrowser) {
            //                //todo: shouldn't need
            //                component!!.addMouseMotionListener(object : MouseMotionListener {
            //                    override fun mouseMoved(e: MouseEvent) {
            //                        e.consume()
            //                    }
            //
            //                    override fun mouseDragged(e: MouseEvent) {
            //                        e.consume()
            //                    }
            //                })
            //            }

            val popup: Disposable
            val popupComponent = gutterMarkComponent.getComponent()
            val dynamicSize = gutterMarkComponent.configuration.componentSizeEvaluator.getDynamicSize(
                    editor, gutterMarkComponent.configuration)
            if (dynamicSize != null && popupComponent.preferredSize != dynamicSize) {
                popupComponent.preferredSize = dynamicSize
            }
            val popupComponentSize = popupComponent.preferredSize

            if (gutterMarkComponent.configuration.useHeavyPopup) {
                if ((this is ClassSourceMark && gutterMarkComponent.configuration.showAboveClass) ||
                        (this is MethodSourceMark && gutterMarkComponent.configuration.showAboveMethod)) {
                    displayPoint.y -= popupComponentSize.height + 4
                }

                popup = JBPopupFactory.getInstance()
                        .createComponentPopupBuilder(popupComponent, popupComponent)
                        .setShowBorder(false)
                        .setShowShadow(false)
                        .setRequestFocus(true)
                        .setCancelOnWindowDeactivation(false)
                        .createPopup()
                popup.addListener(this)
                popup.show(RelativePoint(editor.contentComponent, displayPoint))
            } else {
                val width = (popupComponentSize.width / 2) + 10
                val height = popupComponentSize.height / 2
                displayPoint.x = (displayPoint.getX() + width).toInt()
                displayPoint.y = (displayPoint.getY() - height).toInt()
                popup = JBPopupFactory.getInstance()
                        .createBalloonBuilder(popupComponent)
                        .setBorderInsets(JBUI.emptyInsets())
                        .setDialogMode(true)
                        .setFillColor(JBColor.background())
                        .setAnimationCycle(0)
                        .createBalloon() as BalloonImpl
                popup.addListener(this)
                popup.setShowPointer(false)
                popup.show(RelativePoint(editor.contentComponent, displayPoint), Balloon.Position.atRight)
            }
            visiblePopup = popup
            openedMarks.add(this)

            //dispose popup when mouse hovers off popup
            if (gutterMarkComponent.configuration.hideOnMouseMotion) {
                editor.contentComponent.addMouseMotionListener(this)
                gutterMarkComponent.configuration.addedMouseMotionListener = true
            }
            //dispose popup when code has been scrolled
            if (gutterMarkComponent.configuration.hideOnScroll) {
                editor.scrollingModel.addVisibleAreaListener(this)
                gutterMarkComponent.configuration.addedScrollListener = true
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun dispose(removeFromMarker: Boolean) {
        closePopup()

        if (removeFromMarker) {
            check(sourceFileMarker.removeSourceMark(this, autoRefresh = true, autoDispose = false))
        }
        triggerEvent(SourceMarkEvent(this, SourceMarkEventCode.MARK_REMOVED))
    }

    //region Popup Listeners

    override fun beforeShown(event: LightweightWindowEvent) {
        log.debug("Before popup shown")

        //delay prevents component stains when mark is closed and opened quickly
        //todo: open intellij bug
        Timer().schedule(500) {
            buildingPopup.set(false)
        }
    }

    override fun onClosed(event: LightweightWindowEvent) {
        closePopup()
    }

    override fun visibleAreaChanged(e: VisibleAreaEvent) {
        if (buildingPopup.get()) {
            return //todo: piggy backed on above hack; needed for when navigating from different files
        } else if (e.oldRectangle.location == e.newRectangle.location) {
            return //no change in location
        }

        log.debug("Visible area changed")
        closePopup()
    }

    override fun mouseDragged(e2: MouseEvent) {}
    override fun mouseMoved(e2: MouseEvent) {
        //13 pixels on x coordinate puts mouse past gutter
        if (e2.point.getX() > 13) {
            log.debug("Mouse moved outside popup")
            closePopup()
        }
    }

    //endregion
}