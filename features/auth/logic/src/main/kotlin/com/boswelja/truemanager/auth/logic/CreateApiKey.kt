package com.boswelja.truemanager.auth.logic

import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.Authorization
import com.boswelja.truemanager.core.api.v2.apikey.AllowRule
import com.boswelja.truemanager.core.api.v2.apikey.ApiKeyV2Api

/**
 * Create an API key on a server. See [invoke] for details.
 */
class CreateApiKey(
    private val apiStateProvider: ApiStateProvider,
    private val apiKeyV2Api: ApiKeyV2Api,
) {

    /**
     * Uses the given [username] and [password] to create an API key on the target server.
     */
    suspend operator fun invoke(
        serverAddress: String,
        username: String,
        password: String,
        keyName: String,
    ): Result<String> = runCatching {
        apiStateProvider.serverAddress = serverAddress
        apiStateProvider.authorization = Authorization.Basic(username, password)
        try {
            apiKeyV2Api.create(keyName, listOf(AllowRule("*", "*"))).key
        } finally {
            apiStateProvider.serverAddress = null
            apiStateProvider.authorization = null
        }
    }
}
