package com.nasdroid.auth.logic

import com.nasdroid.api.v2.system.SystemV2Api

/**
 * Tests an existing authentication token is still operational for a server. See [invoke] for details.
 */
class TestServerToken(
    private val apiStateProvider: com.nasdroid.api.v2.ApiStateProvider,
    private val systemV2Api: SystemV2Api,
) {

    /**
     * Tests [apiKey] still functions correctly on the server at [serverAddress].
     */
    suspend operator fun invoke(
        serverAddress: String,
        apiKey: String
    ): Result<Unit> = runCatching {
        apiStateProvider.serverAddress = serverAddress
        apiStateProvider.authorization = com.nasdroid.api.v2.Authorization.ApiKey(apiKey)
        try {
            // Getting the host ID requires valid authentication. If it's not valid this will throw.
            val token = systemV2Api.getHostId()
            check(token.isNotBlank()) { "The server returned a blank ID!" }
        } finally {
            apiStateProvider.authorization = null
            apiStateProvider.serverAddress = null
        }
    }
}
