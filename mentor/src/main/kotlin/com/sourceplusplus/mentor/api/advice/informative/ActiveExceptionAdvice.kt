package com.sourceplusplus.mentor.api.advice.informative

import com.sourceplusplus.protocol.advice.AdviceType
import com.sourceplusplus.protocol.advice.ArtifactAdvice

class ActiveExceptionAdvice : ArtifactAdvice {

    //todo: get active service instance
    //todo: find failing traces
    //todo: determine failing location
    //todo: create advice
    //todo: maintain created advice status (remove on new instances, etc)

    override val type: AdviceType = AdviceType.INFORMATIVE
}