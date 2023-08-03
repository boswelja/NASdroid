package com.boswelja.truemanager.auth.logic.addserver

import com.boswelja.truemanager.core.api.v2.system.SystemV2Api

class AuthenticateAndAddServer(
    private val systemV2Api: SystemV2Api,
    private val createApiKey: CreateApiKey,
    private val testApiKey: TestApiKey,
    private val addNewServer: AddNewServer,
) {

    suspend operator fun invoke(
        serverName: String,
        serverAddress: String,
        username: String,
        password: String
    ): Result<Unit> = runCatching {
        val apiKey = createApiKey(
            serverAddress = serverAddress,
            username = username,
            password = password,
            keyName = "TrueManager for TrueNAS"
        ).getOrThrow()

        invoke(
            serverName = serverName,
            serverAddress = serverAddress,
            apiKey = apiKey
        ).getOrThrow()
    }

    suspend operator fun invoke(
        serverName: String,
        serverAddress: String,
        apiKey: String
    ): Result<Unit> = runCatching {
        testApiKey(serverAddress, apiKey).getOrThrow()

        val actualName = serverName.ifBlank {
            val systemInfo = systemV2Api.getSystemInfo()
            systemInfo.systemProduct
        }
        val uid = systemV2Api.getHostId()

        addNewServer(
            serverUid = uid,
            serverAddress = serverAddress,
            apiKey = apiKey,
            serverName = actualName
        )
    }
}
