package com.nasdroid.api.websocket.apiKey

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes the TrueNAS API "api_key" group.
 */
interface ApiKeyApi {

    /**
     * Creates API Key.
     */
    suspend fun create(newKey: NewApiKey): ApiKey

    /**
     * Delete API Key [id].
     */
    suspend fun delete(id: Int): Boolean

    /**
     * Returns instance matching [id]. If id is not found, Validation error is raised.
     */
    suspend fun getInstance(id: Int): ApiKey

    // TODO query?

    /**
     * Update API Key [id].
     */
    suspend fun update(id: Int, updatedKey: UpdateApiKey): ApiKey
}

/**
 * Represents an API Key returned by the TrueNAS API. Contains metadata and access details for the key.
 *
 * @property id The unique identifier for the API Key.
 * @property name The name of the API Key.
 * @property createdAt The timestamp when the API Key was created.
 * @property allowList The list of allowed methods and resources for this API Key.
 * @property key The actual API Key string. This should be kept secure.
 */
@Serializable
data class ApiKey(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("allowlist")
    val allowList: List<AllowedMethod>,
    @SerialName("key")
    val key: String,
)

/**
 * Describes an update to an existing API key, where any non-null fields are updated.
 *
 * @property name The name of the key. Must not exceed 200 characters.
 * @property allowList A list of [AllowedMethod]s that this API key should have access to.
 * @property reset Whether the key should be reset at the same time.
 */
@Serializable
data class UpdateApiKey(
    @SerialName("name")
    val name: String?,
    @SerialName("allowlist")
    val allowList: List<AllowedMethod>?,
    @SerialName("reset")
    val reset: Boolean?
)

/**
 * Describes a new API key to be created.
 *
 * @property name The name of the key. Must not exceed 200 characters.
 * @property allowList A list of [AllowedMethod]s that this API key should have access to.
 */
@Serializable
data class NewApiKey(
    @SerialName("name")
    val name: String,
    @SerialName("allowlist")
    val allowList: List<AllowedMethod>
)

/**
 * Represents a method and resource that an API key is allowed to access.
 *
 * @property method The HTTP/Websocket method that is allowed.
 * @property resource The specific resource that is allowed, or `*` for all resources.
 */
@Serializable
data class AllowedMethod(
    @SerialName("method")
    val method: Method,
    @SerialName("resource")
    val resource: String
) {
    /**
     * The HTTP/Websocket methods that an API key can be allowed to use.
     */
    @Serializable
    enum class Method {
        @SerialName("GET")
        Get,
        @SerialName("POST")
        Post,
        @SerialName("PUT")
        Put,
        @SerialName("DELETE")
        Delete,
        @SerialName("CALL")
        Call,
        @SerialName("SUBSCRIBE")
        Subscribe,
        @SerialName("*")
        All,
    }
}