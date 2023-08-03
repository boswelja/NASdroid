package com.boswelja.truemanager.auth.logic.addserver

import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.Authorization
import com.boswelja.truemanager.core.api.v2.system.SystemV2Api

class TestApiKey(
    private val apiStateProvider: ApiStateProvider,
    private val systemV2Api: SystemV2Api,
) {

    suspend operator fun invoke(
        serverAddress: String,
        apiKey: String
    ): Result<Unit> = runCatching {
        apiStateProvider.serverAddress = serverAddress
        apiStateProvider.authorization = Authorization.ApiKey(apiKey)
        try {
            // Getting the host ID requires valid authentication. If it's not valid this will throw.
            systemV2Api.getHostId()
        } finally {
            apiStateProvider.authorization = null
            apiStateProvider.serverAddress = null
        }
    }
}
