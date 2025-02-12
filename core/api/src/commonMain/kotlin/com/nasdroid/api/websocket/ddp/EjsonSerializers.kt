package com.nasdroid.api.websocket.ddp

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer

/**
 * A convenience serializer for mapping
 * [EJSON Dates](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#appendix-ejson) to
 * milliseconds since epoch.
 */
class EDateMillisSerializer : JsonTransformingSerializer<Long>(Long.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element is JsonObject && element.contains("\$date")) {
            return element.getValue("\$date")
        }
        return super.transformDeserialize(element)
    }

    override fun transformSerialize(element: JsonElement): JsonElement {
        return JsonObject(
            mapOf("\$date" to element)
        )
    }
}

/**
 * A convenience serializer for mapping
 * [EJSON Dates](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#appendix-ejson) to
 * an [Instant].
 */
class EDateInstantSerializer : JsonTransformingSerializer<Instant>(InstantEpochMillisSerializer) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element is JsonObject && element.contains("\$date")) {
            return element.getValue("\$date")
        }
        return super.transformDeserialize(element)
    }

    override fun transformSerialize(element: JsonElement): JsonElement {
        return JsonObject(
            mapOf("\$date" to element)
        )
    }
}

internal object InstantEpochMillisSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("kotlinx.datetime.Instant", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeLong(value.toEpochMilliseconds())
    }

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.fromEpochMilliseconds(decoder.decodeLong())
    }
}

/**
 * A convenience serializer for mapping
 * [EJSON Binary](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#appendix-ejson) to
 * a base64-encoded String.
 */
class EBinarySerializer : JsonTransformingSerializer<String>(String.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element is JsonObject && element.contains("\$binary")) {
            return element.getValue("\$binary")
        }
        return super.transformDeserialize(element)
    }

    override fun transformSerialize(element: JsonElement): JsonElement {
        return JsonObject(
            mapOf("\$binary" to element)
        )
    }
}
