package com.sourceplusplus.monitor.skywalking

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import java.util.*

/**
 * todo: description.
 *
 * @since 0.0.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
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
