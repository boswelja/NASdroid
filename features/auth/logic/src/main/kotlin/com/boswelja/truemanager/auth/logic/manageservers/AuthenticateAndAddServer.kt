package com.boswelja.truemanager.auth.logic.manageservers

import com.boswelja.truemanager.auth.logic.CreateApiKey
import com.boswelja.truemanager.auth.logic.TestApiKey
import com.boswelja.truemanager.auth.logic.then
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
    ): Result<Unit> =
        createApiKey(
            serverAddress = serverAddress,
            username = username,
            password = password,
            keyName = "TrueManager for TrueNAS"
        ).then { apiKey ->
            invoke(
                serverName = serverName,
                serverAddress = serverAddress,
                apiKey = apiKey
            )
        }

    suspend operator fun invoke(
        serverName: String,
        serverAddress: String,
        apiKey: String
    ): Result<Unit> =
        testApiKey(serverAddress, apiKey)
            .then {
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
