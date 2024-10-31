package com.nasdroid.api.websocket.message

import kotlinx.datetime.Instant
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer

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

class EDateInstantSerializer : JsonTransformingSerializer<Instant>(Instant.serializer()) {
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
