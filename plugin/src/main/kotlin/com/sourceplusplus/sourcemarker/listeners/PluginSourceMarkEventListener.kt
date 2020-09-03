package com.sourceplusplus.sourcemarker.listeners

import com.sourceplusplus.marker.source.mark.api.MethodSourceMark
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEvent
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventCode
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventListener
import com.sourceplusplus.mentor.api.impl.SourceMentorImpl

class PluginSourceMarkEventListener : SourceMarkEventListener {

    //private val sourceMentor = SourceMentorImpl()

    override fun handleEvent(event: SourceMarkEvent) {
        if (event.eventCode == SourceMarkEventCode.MARK_ADDED) {
            if (event.sourceMark is MethodSourceMark) {
                //todo: gather and display markings
                //todo: gather and display advice
            }
        }
    }
}
