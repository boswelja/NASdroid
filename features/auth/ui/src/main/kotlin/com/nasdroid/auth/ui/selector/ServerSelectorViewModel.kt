package com.nasdroid.auth.ui.selector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.auth.logic.Server
import com.nasdroid.auth.logic.auth.GetCurrentServer
import com.nasdroid.auth.logic.auth.LogOut
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

/**
 * Exposes all data and events needed for a selector that displays the current selected server, as
 * well as controls to log out or switch accounts.
 */
class ServerSelectorViewModel(
    private val logOut: LogOut,
    getCurrentServer: GetCurrentServer,
) : ViewModel() {

    /**
     * Flows the currently selected server.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedServer: StateFlow<Server> = getCurrentServer()
        .mapLatest { it ?: FallbackServerDetails }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            FallbackServerDetails
        )

    /**
     * Logs the user out of the current server.
     */
    fun logOut() {
        logOut.invoke()
    }

    companion object {
        private val FallbackServerDetails = Server(
            name = "Nothing",
            url = "Please report this",
            id = ""
        )
    }
}
