package com.boswelja.truemanager.auth.logic.auth

import com.boswelja.truemanager.core.api.v2.ApiStateProvider

class LogOut(
    private val apiStateProvider: ApiStateProvider,
) {

    operator fun invoke() {
        apiStateProvider.serverAddress = null
        apiStateProvider.authorization = null
    }
}
