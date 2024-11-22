package com.nasdroid.api.websocket.ddp

sealed interface SubscriptionEvent<T> {
    data class DocumentAdded<T>(
        val collection: String,
        val id: String,
        val document: T,
        val beforeId: String?
    ): SubscriptionEvent<T>

    data class DocumentRemoved<T>(
        val collection: String,
        val id: String
    ): SubscriptionEvent<T>

    data class DocumentChanged<T>(
        val collection: String,
        val id: String,
        val clearedFields: List<String>,
        val updatedFields: Map<String, Any?>,
    ): SubscriptionEvent<T>

    data class DocumentMoved<T>(
        val collection: String,
        val id: String,
        val movedBefore: String?
    ): SubscriptionEvent<T>
}
