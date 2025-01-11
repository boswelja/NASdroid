package com.nasdroid.api.websocket.ddp

/**
 * Encapsulates all possible events that might happen on subscribed data. Subscribe to a collection
 * via [DdpWebsocketClient.subscribe].
 */
sealed interface SubscriptionEvent<T> {

    /**
     * A new document was added to the collection.
     *
     * @param T The data type of the document.
     *
     * @property collection The name of the collection this document was added to.
     * @property id The ID of the new document.
     * @property document The data contained within the document.
     * @property beforeId If the document was added before an existing document, this holds the ID
     * of that document.
     */
    data class DocumentAdded<T>(
        val collection: String,
        val id: String,
        val document: T,
        val beforeId: String?
    ): SubscriptionEvent<T>

    /**
     * An existing document was removed from the collection.
     *
     * @param T The data type of the document.
     *
     * @property collection The name of the collection this document was removed from.
     * @property id The ID of the document that was removed from the collection.
     */
    data class DocumentRemoved<T>(
        val collection: String,
        val id: String
    ): SubscriptionEvent<T>

    /**
     * A new document was added to the collection.
     *
     * @param T The data type of the document.
     *
     * @property collection The name of the collection this document was added to.
     * @property id The ID of the new document.
     * @property clearedFields A list of field names that were removed from the document.
     * @property updatedFields A map of field names to new values for existing fields within the
     * document.
     */
    data class DocumentChanged<T>(
        val collection: String,
        val id: String,
        val clearedFields: List<String>,
        val updatedFields: Map<String, Any?>,
    ): SubscriptionEvent<T>

    /**
     * An existing document in the collection was moved to a new location.
     *
     * @param T The data type of the document.
     *
     * @property collection The name of the collection this document belongs to.
     * @property id The ID of the document in the collection.
     * @property movedBefore If the document was moved before an existing document, this holds the
     * ID of that document. Otherwise, if the document was moved to the end of the collection this
     * is null.
     */
    data class DocumentMoved<T>(
        val collection: String,
        val id: String,
        val movedBefore: String?
    ): SubscriptionEvent<T>
}
