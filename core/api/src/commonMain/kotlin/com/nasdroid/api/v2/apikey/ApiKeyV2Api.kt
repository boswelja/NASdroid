package com.nasdroid.api.v2.apikey

import com.nasdroid.api.exception.HttpNotOkException
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes the TrueNAS API V2 "API key" group. Note these mappings may not be 1:1, as we will
 * rearrange data to be more accessible in Kotlin.
 */
interface ApiKeyV2Api {

    /**
     * Get a list of all API keys.
     *
     * @param limit The maximum number of API keys to retrieve.
     * @param offset The number of API keys to skip before taking [limit] keys.
     * @param sort The sorting method for keys.
     *
     * @throws HttpNotOkException
     */
    suspend fun getAll(limit: Int?, offset: Int?, sort: String?): List<ApiKey>

    /**
     * Create a new API key with the given name.
     *
     * @throws HttpNotOkException
     */
    suspend fun create(name: String, allowItems: List<AllowRule>): NewApiKey

    /**
     * Delete an API key with the given ID.
     *
     * @return Whether the API key was deleted successfully.
     *
     * @throws HttpNotOkException
     */
    suspend fun delete(id: Int): Boolean

    /**
     * Get an API key with the given ID.
     *
     * @throws HttpNotOkException
     */
    suspend fun get(id: Int): ApiKey

    /**
     * Update the name of the API key with the given ID.
     *
     * @throws HttpNotOkException
     */
    suspend fun update(id: Int, updatedDetails: UpdateApiKey): ApiKey

    /**
     * Regenerate the API key with the given ID.
     * @return The new generated key.
     *
     * @throws HttpNotOkException
     */
    suspend fun reset(id: Int): NewApiKey
}

/**
 * Describes a request to update specified fields for an API key. If a value is null, it will not
 * be changed.
 *
 * @property name The name of the API key. This is visible to and adjustable by the user.
 * @property allowList The list of [AllowRule]s for the API key.
 *
 * @throws HttpNotOkException
 */
@Serializable
data class UpdateApiKey(
    val name: String? = null,
    val allowList: List<AllowRule>? = null,
)

/**
 * Describes an API key on the system.
 *
 * @property id A unique identifier for this API key.
 * @property name The name the user has given the API key.
 * @property createdAt When this API key was created.
 * @property allowList A list of [AllowRule]s for this key.
 *
 * @throws HttpNotOkException
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
    val allowList: List<AllowRule>
)

/**
 * Describes a newly created API key on the system.
 *
 * @property id A unique identifier for this API key.
 * @property name The name the user has given the API key.
 * @property createdAt When this API key was created.
 * @property allowList A list of [AllowRule]s for this key.
 * @property key The newly created API key.
 *
 * @throws HttpNotOkException
 */
@Serializable
data class NewApiKey(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("allowlist")
    val allowList: List<AllowRule>,
    @SerialName("key")
    val key: String,
)

/**
 * Describes what an API key is allowed to access.
 *
 * @property method The request method type this API key can access. For example, 'GET', 'POST', '*".
 * @property resource The resource name this API key can access. For example, '/system/info', '*'.
 *
 * @throws HttpNotOkException
 */
@Serializable
data class AllowRule(
    @SerialName("method")
    val method: String,
    @SerialName("resource")
    val resource: String,
)
