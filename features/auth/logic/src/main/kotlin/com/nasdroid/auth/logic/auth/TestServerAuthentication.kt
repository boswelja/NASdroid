package com.nasdroid.auth.logic.auth

import com.nasdroid.api.ApiStateProvider
import com.nasdroid.api.Authorization
import com.nasdroid.api.v2.system.SystemV2Api

/**
 * Tests the provided authentication information functions against a specified server. See [invoke]
 * for details.
 */
class TestServerAuthentication(
    private val apiStateProvider: ApiStateProvider,
    private val systemV2Api: SystemV2Api,
) {

    /**
     * Tests the given [username] and [password] combination work for the server at [serverUrl].
     */
    suspend operator fun invoke(serverUrl: String, username: String, password: String): Result<Unit> = runCatching {
        apiStateProvider.serverAddress = serverUrl
        apiStateProvider.authorization = Authorization.Basic(username, password)
        try {
            testAuthentication()
        } finally {
            apiStateProvider.authorization = null
            apiStateProvider.serverAddress = null
        }
    }

    /**
     * Tests the given [apiKey] works for the server at [serverUrl].
     */
    suspend operator fun invoke(serverUrl: String, apiKey: String): Result<Unit> = runCatching {
        apiStateProvider.serverAddress = serverUrl
        apiStateProvider.authorization = Authorization.ApiKey(apiKey)
        try {
            testAuthentication()
        } finally {
            apiStateProvider.authorization = null
            apiStateProvider.serverAddress = null
        }
    }

    private suspend fun testAuthentication(): Result<Unit> = runCatching {
        // Getting the host ID requires valid authentication. If it's not valid this will throw.
        val token = systemV2Api.getHostId()
        check(token.isNotBlank()) { "The server returned a blank ID!" }
    }
}
