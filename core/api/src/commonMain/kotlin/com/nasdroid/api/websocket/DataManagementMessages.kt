package com.nasdroid.api.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubMessage(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("params")
    val params: List<String>? = null,
) {
    @SerialName("msg")
    val message: String = "sub"
}

@Serializable
data class UnsubMessage(
    @SerialName("id")
    val id: String
) {
    @SerialName("msg")
    val message: String = "unsub"
}

@Serializable
data class NosubMessage(
    @SerialName("id")
    val id: String,
    @SerialName("error")
    val error: String? = null
) {
    @SerialName("msg")
    val message: String = "nosub"
}

@Serializable
data class AddedMessage<T>(
    @SerialName("id")
    val id: String,
    @SerialName("collection")
    val collection: String,
    @SerialName("fields")
    val fields: T? = null
) {
    @SerialName("msg")
    val message: String = "added"
}

@Serializable
data class ChangedMessage<T>(
    @SerialName("id")
    val id: String,
    @SerialName("collection")
    val collection: String,
    @SerialName("fields")
    val fields: T? = null,
    @SerialName("cleared")
    val cleared: List<String>? = null
) {
    @SerialName("msg")
    val message: String = "changed"
}

@Serializable
data class RemovedMessage(
    @SerialName("id")
    val id: String,
    @SerialName("collection")
    val collection: String
) {
    @SerialName("msg")
    val message: String = "removed"
}

@Serializable
data class ReadyMessage(
    @SerialName("subs")
    val subs: List<String>
) {
    @SerialName("msg")
    val message: String = "ready"
}

@Serializable
data class AddedBeforeMessage<T>(
    @SerialName("id")
    val id: String,
    @SerialName("collection")
    val collection: String,
    @SerialName("fields")
    val fields: T? = null,
    @SerialName("before")
    val before: String? = null
) {
    @SerialName("msg")
    val message: String = "addedBefore"
}

@Serializable
data class MovedBeforeMessage(
    @SerialName("id")
    val id: String,
    @SerialName("collection")
    val collection: String,
    @SerialName("before")
    val before: String? = null
) {
    @SerialName("msg")
    val message: String = "movedBefore"
}
