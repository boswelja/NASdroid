package com.boswelja.truemanager.core.api.v2.apikey

import kotlinx.datetime.Instant

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
     */
    suspend fun getAll(limit: Int?, offset: Int?, sort: String?): List<ApiKey>

    /**
     * Create a new API key with the given name.
     */
    suspend fun create(name: String): String

    /**
     * Delete an API key with the given ID.
     */
    suspend fun delete(id: Int)

    /**
     * Get an API key with the given ID.
     */
    suspend fun get(id: Int): ApiKey

    /**
     * Update the name of the API key with the given ID.
     */
    suspend fun update(id: Int, name: String)

    /**
     * Regenerate the API key with the given ID.
     * @return The new generated key.
     */
    suspend fun reset(id: Int): String
}

/**
 * Describes an API key on the system.
 *
 * @property id A unique identifier for this API key.
 * @property name The name the user has given the API key.
 * @property createdAt When this API key was created.
 * @property allowList A list of [AllowRule]s for this key.
 */
data class ApiKey(
    val id: Int,
    val name: String,
    val createdAt: Instant,
    val allowList: List<AllowRule>
) {
    /**
     * Describes what an API key is allowed to access.
     *
     * @property method The request method type this API key can access. For example, 'GET', 'POST', '*".
     * @property resource The resource name this API key can access. For example, '/system/info', '*'.
     */
    data class AllowRule(
        val method: String,
        val resource: String,
    )
}
