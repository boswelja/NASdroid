package com.nasdroid.api.websocket.ddp.message

import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

sealed interface ClientMessage

sealed interface ServerMessage

object ServerMessageSerializer : JsonContentPolymorphicSerializer<ServerMessage>(ServerMessage::class) {
    @OptIn(ExperimentalSerializationApi::class)
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ServerMessage> {
        return when (element.jsonObject["msg"]?.jsonPrimitive?.content) {
            // Connect messages
            "connected" -> ConnectedMessage.serializer()
            "failed" -> FailedMessage.serializer()
            // Data management messages
            "nosub" -> NosubMessage.serializer()
            "added" -> AddedMessage.serializer(ContextualSerializer(Any::class))
            "changed" -> ChangedMessage.serializer(ContextualSerializer(Any::class))
            "removed" -> RemovedMessage.serializer()
            "ready" -> ReadyMessage.serializer()
            "addedBefore" -> AddedBeforeMessage.serializer(ContextualSerializer(Any::class))
            "movedBefore" -> MovedBeforeMessage.serializer()
            // Generic error messages
            "error" -> ErrorMessage.serializer()
            // Heartbeat messages
            "ping" -> PingMessage.serializer()
            "pong" -> PongMessage.serializer()
            // RPC messages
            "result" -> ResultMessage.serializer(ContextualSerializer(Any::class))
            "updated" -> UpdatedMessage.serializer()
            else -> error("Unknown message type for ConnectServerMessage: $element")
        }
    }
}
