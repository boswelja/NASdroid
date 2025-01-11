package com.nasdroid.api.websocket.alert

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface AlertApi {
    /**
     * Dismiss [uuid] alert.
     */
    suspend fun dismiss(uuid: String)

    /**
     * List all types of alerts including active/dismissed currently in the system.
     */
    suspend fun list(): List<Alert>

    /**
     * List all types of alerts which the system can issue.
     */
    suspend fun listCategories(): List<AlertCategory>
    /**
     * List all alert policies which indicate the frequency of the alerts.
     */
    suspend fun listPolicies(): List<AlertPolicy>

    /**
     * Restore [uuid] alert which had been dismissed.
     */
    suspend fun restore(uuid: String)
}

@Serializable
data class AlertCategory(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("classes")
    val classes: List<Class>,
) {
    @Serializable
    data class Class(
        @SerialName("id")
        val id: String,
        @SerialName("title")
        val title: String,
        @SerialName("level")
        val level: String,
        @SerialName("proactive_support")
        val proactiveSupport: Boolean
    )
}

@Serializable
enum class AlertPolicy {
    @SerialName("IMMEDIATELY")
    Immediately,
    @SerialName("HOURLY")
    Hourly,
    @SerialName("DAILY")
    Daily,
    @SerialName("NEVER")
    Never
}

@Serializable
data class Alert(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("source")
    val source: String,
    @SerialName("klass")
    val klass: String,
    @SerialName("args")
    val args: List<String>,
    @SerialName("node")
    val node: String,
    @SerialName("key")
    val key: String,
    @SerialName("datetime")
    val datetime: String,
    @SerialName("last_occurrence")
    val lastOccurrence: String,
    @SerialName("dismissed")
    val dismissed: Boolean,
    @SerialName("mail")
    val mail: List<String>,
    @SerialName("text")
    val text: String,
    @SerialName("id")
    val id: String,
    @SerialName("level")
    val level: String,
    @SerialName("formatted")
    val formatted: String?,
    @SerialName("one_shot")
    val oneShot: Boolean,
)
