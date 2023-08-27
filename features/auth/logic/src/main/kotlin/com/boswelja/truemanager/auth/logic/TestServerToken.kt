package com.boswelja.truemanager.auth.logic

import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.Authorization
import com.boswelja.truemanager.core.api.v2.system.SystemV2Api

/**
 * Tests an existing authentication token is still operational for a server. See [invoke] for details.
 */
class TestServerToken(
    private val apiStateProvider: ApiStateProvider,
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
        apiStateProvider.authorization = Authorization.ApiKey(apiKey)
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
