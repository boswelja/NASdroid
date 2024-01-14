package com.nasdroid.auth.ui.selector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.auth.logic.Server
import com.nasdroid.auth.logic.auth.LogIn
import com.nasdroid.auth.logic.auth.LogOut
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Exposes all data and events needed for a selector that displays the current selected server, as
 * well as controls to log out or switch accounts.
 */
class ServerSelectorViewModel(
    private val logOut: LogOut,
    private val logIn: LogIn,
) : ViewModel() {

    /**
     * Flows the currently selected server.
     */
    val selectedServer: StateFlow<Server> = MutableStateFlow(
        Server(name = "Server name", url = "http://truenas.local", id = "")
    )

    /**
     * Logs the user out of the current server.
     */
    fun logOut() {
        logOut.invoke()
    }

    /**
     * Switches the current authenticated server to the new selection.
     */
    fun switchServer(server: Server) {
        viewModelScope.launch {
            logIn.invoke(server)
        }
    }
}
