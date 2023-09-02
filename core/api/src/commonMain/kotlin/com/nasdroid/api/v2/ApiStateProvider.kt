package com.nasdroid.api.v2

/**
 * Provides various common states needed for the API to function.
 */
interface ApiStateProvider {

    /**
     * The base address of the server, for example 'http://truenas.local'. This cannot be null when
     * making API calls.
     */
    var serverAddress: String?

    /**
     * An [Authorization] to use for each request. This cannot be null when making API calls.
     */
    var authorization: com.nasdroid.api.v2.Authorization?
}

/**
 * Describes possible API authorization types.
 */
sealed class Authorization {

    /**
     * Basic (username and password) authorization.
     *
     * @property username The username for auth.
     * @property password The password for auth.
     */
    data class Basic(val username: String, val password: String) : com.nasdroid.api.v2.Authorization()

    /**
     * Authorization via API key.
     *
     * @property apiKey The API key to use for authorization
     */
    data class ApiKey(val apiKey: String) : com.nasdroid.api.v2.Authorization()
}

internal class InMemoryApiStateProvider : com.nasdroid.api.v2.ApiStateProvider {
    override var serverAddress: String? = null
        set(value) {
            if (field != value) {
                if (value == null) {
                    field = null
                    return
                }

                field = "${value.removeSuffix("/")}/api/v2.0/"
            }
        }

    override var authorization: com.nasdroid.api.v2.Authorization? = null
}
