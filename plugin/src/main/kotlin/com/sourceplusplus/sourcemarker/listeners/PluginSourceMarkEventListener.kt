package com.sourceplusplus.sourcemarker.listeners

import com.sourceplusplus.marker.source.mark.api.MethodSourceMark
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEvent
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventCode
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventListener
import com.sourceplusplus.mentor.api.impl.SourceMentorImpl

class PluginSourceMarkEventListener : SourceMarkEventListener {

    private val sourceMentor = SourceMentorImpl()

    override fun handleEvent(event: SourceMarkEvent) {
        if (event.eventCode == SourceMarkEventCode.MARK_ADDED) {
            //todo: query apache skywalking
            if (event.sourceMark is MethodSourceMark) {
                println("here")
//                sourceMentor.getAllMethodAdvice(event.sourceMark.)
            }
        }
    }
}