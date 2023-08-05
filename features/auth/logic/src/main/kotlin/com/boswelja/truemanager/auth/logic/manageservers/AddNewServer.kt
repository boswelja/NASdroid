package com.boswelja.truemanager.auth.logic.manageservers

import com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServer
import com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServersStore

class AddNewServer(
    private val authedServersStore: AuthenticatedServersStore,
) {

    suspend operator fun invoke(
        server: Server,
        apiKey: String
    ): Result<Unit> = runCatching {
        authedServersStore.add(
            AuthenticatedServer(
                uid = server.id,
                serverAddress = server.url,
                token = apiKey,
                name = server.name
            )
        )
    }
}
