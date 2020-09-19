package com.sourceplusplus.sourcemarker.activities

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import java.util.*

class LocalMessageCodec<T> internal constructor(private val type: Class<T>) : MessageCodec<T, T> {
    override fun encodeToWire(buffer: Buffer, o: T) {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun decodeFromWire(pos: Int, buffer: Buffer): T {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun transform(o: T): T {
        return o
    }

    override fun name(): String {
        return UUID.randomUUID().toString()
    }

    override fun systemCodecID(): Byte {
        return -1
    }

    fun type(): Class<T> {
        return type
    }
}