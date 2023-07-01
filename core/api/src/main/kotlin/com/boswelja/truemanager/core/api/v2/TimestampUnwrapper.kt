package com.boswelja.truemanager.core.api.v2

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer

internal class TimestampUnwrapper : JsonTransformingSerializer<Long>(Long.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element is JsonObject && element.contains("\$date")) {
            return element.getValue("\$date")
        }
        return super.transformDeserialize(element)
    }
}