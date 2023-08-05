package com.boswelja.truemanager.auth.logic.auth

import com.boswelja.truemanager.core.api.v2.ApiStateProvider

/**
 * Signs out the current user. See [invoke] for details.
 */
class LogOut(
    private val apiStateProvider: ApiStateProvider,
) {

    /**
     * Signs out the current user. If no user is signed in, nothing happens. This is guaranteed to
     * result in a "signed out" state.
     */
    operator fun invoke() {
        apiStateProvider.serverAddress = null
        apiStateProvider.authorization = null
    }
}
