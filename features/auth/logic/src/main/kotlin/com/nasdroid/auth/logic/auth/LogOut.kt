package com.nasdroid.auth.logic.auth

import com.nasdroid.api.ApiStateProvider
import com.nasdroid.auth.data.currentserver.CurrentServerSource

/**
 * Signs out the current user. See [invoke] for details.
 */
class LogOut(
    private val apiStateProvider: ApiStateProvider,
    private val currentServerSource: CurrentServerSource,
) {

    /**
     * Signs out the current user. If no user is signed in, nothing happens. This is guaranteed to
     * result in a "signed out" state.
     */
    operator fun invoke() {
        apiStateProvider.serverAddress = null
        apiStateProvider.authorization = null
        currentServerSource.setCurrentServer(null)
    }
}
