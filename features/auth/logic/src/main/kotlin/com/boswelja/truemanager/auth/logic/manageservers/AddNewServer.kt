package com.boswelja.truemanager.auth.logic.manageservers

import com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServer
import com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServersStore

class AddNewServer(
    private val authedServersStore: AuthenticatedServersStore,
) {

    suspend operator fun invoke(
        serverUid: String,
        serverName: String,
        serverAddress: String,
        apiKey: String
    ): Result<Unit> = runCatching {
        authedServersStore.add(
            AuthenticatedServer(
                uid = serverUid,
                serverAddress = serverAddress,
                token = apiKey,
                name = serverName
            )
        )
    }
}
