package com.nasdroid.api.websocket.ddp.message

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Seals concrete definitions for
 * [DDP data management messages](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#managing-data).
 * These messages are sent to the server to negotiate data observation.
 */
@Serializable
internal sealed interface DataManagementClientMessage : ClientMessage

/**
 * Seals concrete definitions for
 * [DDP data management messages](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#managing-data).
 * These messages are received from the server to negotiate data observation.
 */
@Serializable
internal sealed interface DataManagementServerMessage : ServerMessage

/**
 * Sent by the client to subscribe to a set of information.
 *
 * @property id An arbitrary client-determined identifier for this subscription.
 * @property name The name of the subscription.
 * @property params Optional parameters to the subscription.
 */
@Serializable
internal data class SubMessage<P>(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("params")
    val params: List<P>? = null,
) : DataManagementClientMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "sub"
}

/**
 * Sent by the client to unsubscribe from a set of information.
 *
 * @property id The [SubMessage.id].
 */
@Serializable
internal data class UnsubMessage(
    @SerialName("id")
    val id: String
) : DataManagementClientMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "unsub"
}

/**
 * Received from the server to indicate that the client has successfully unsubscribed from a set of information.
 *
 * @property id The [SubMessage.id].
 * @property error An optional error raised by the subscription as it concludes, or sub-not-found
 */
@Serializable
internal data class NosubMessage(
    @SerialName("id")
    val id: String,
    @SerialName("error")
    val error: Error? = null
) : DataManagementServerMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "nosub"
}

/**
 * Received from the server to indicate that a new piece of information has been added.
 *
 * @property id The ID of the document that was added.
 * @property collection Collection name.
 * @property fields The optional fields of the document that were added.
 */
@Serializable
internal data class AddedMessage<T>(
    @SerialName("id")
    val id: String,
    @SerialName("collection")
    val collection: String,
    @SerialName("fields")
    val fields: T? = null
) : DataManagementServerMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "added"
}

/**
 * Received from the server to indicate that a piece of information has been changed.
 *
 * @property id The ID of the document that was updated.
 * @property collection Collection name.
 * @property fields The optional fields of the document that were updated.
 * @property cleared Optional list of field names that were removed.
 */
@Serializable
internal data class ChangedMessage(
    @SerialName("id")
    val id: String,
    @SerialName("collection")
    val collection: String,
    @SerialName("fields")
    val fields: Map<String, String>? = null,
    @SerialName("cleared")
    val cleared: List<String>? = null
) : DataManagementServerMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "changed"
}

/**
 * Received from the server to indicate that a piece of information has been removed.
 *
 * @property id The ID of the document that was removed.
 * @property collection Collection name.
 */
@Serializable
internal data class RemovedMessage(
    @SerialName("id")
    val id: String,
    @SerialName("collection")
    val collection: String
) : DataManagementServerMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "removed"
}

/**
 * Received from the server when one or more subscriptions have finished sending their initial batch
 * of data.
 *
 * @property subs IA list of [SubMessage.id]s which have sent their initial batch of data
 */
@Serializable
internal data class ReadyMessage(
    @SerialName("subs")
    val subs: List<String>
) : DataManagementServerMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "ready"
}

/**
 * Received from the server to indicate that a new ordered piece of information was added.
 *
 * @property id The ID of the document that was added.
 * @property collection Collection name.
 * @property fields The optional fields of the document that were added.
 * @property before The document ID to add the document before, or null to add at the end
 */
@Serializable
internal data class AddedBeforeMessage<T>(
    @SerialName("id")
    val id: String,
    @SerialName("collection")
    val collection: String,
    @SerialName("fields")
    val fields: T? = null,
    @SerialName("before")
    val before: String? = null
) : DataManagementServerMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "addedBefore"
}

/**
 * Received from the server to indicate that a document was moved to a new position in the list.
 *
 * @property id The ID of the document that was moved.
 * @property collection The name of the collection where the document was moved.
 * @property before The ID of the document that the moved document is now before, or null if it's at the end.
 */
@Serializable
internal data class MovedBeforeMessage(
    @SerialName("id")
    val id: String,
    @SerialName("collection")
    val collection: String,
    @SerialName("before")
    val before: String? = null
) : DataManagementServerMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "movedBefore"
}
