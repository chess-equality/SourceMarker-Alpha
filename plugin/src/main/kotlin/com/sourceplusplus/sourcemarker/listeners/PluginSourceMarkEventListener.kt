package com.sourceplusplus.sourcemarker.listeners

import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEvent
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventCode
import com.sourceplusplus.marker.source.mark.api.event.SourceMarkEventListener

class PluginSourceMarkEventListener : SourceMarkEventListener {

    override fun handleEvent(event: SourceMarkEvent) {
        if (event.eventCode == SourceMarkEventCode.MARK_ADDED) {
            //todo: query apache skywalking
        }
    }
}