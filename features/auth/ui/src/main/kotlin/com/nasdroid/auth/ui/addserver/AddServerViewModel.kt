package com.nasdroid.auth.ui.addserver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.auth.logic.manageservers.AddNewServer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Responsible for exposing data to and handling events from the "add server" UI.
 */
class AddServerViewModel(
    private val addNewServer: AddNewServer,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)

    /**
     * Whether the ViewModel is currently loading data. This should be used to switch the UI to a
     * loading state.
     */
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Try to authenticate via username/password. This will verify the credentials, and try to create
     * an API key.
     *
     * @param serverName An optional name for the server. If this is blank, a name will be retrieved
     * from the server.
     * @param serverAddress The address of the server. For example, `http://truenas.local`.
     * @param username The username to authenticate with.
     * @param password The password to authenticate with.
     */
    fun tryLogIn(
        serverName: String,
        serverAddress: String,
        username: String,
        password: String
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            addNewServer(
                serverName = serverName,
                serverAddress = serverAddress,
                username = username,
                password = password
            )
            _isLoading.value = false
        }
    }

    /**
     * Try to authenticate via API key.
     *
     * @param serverName An optional name for the server. If this is blank, a name will be retrieved
     * from the server.
     * @param serverAddress The address of the server. For example, `http://truenas.local`.
     * @param apiKey The API key to try authenticate with.
     */
    fun tryLogIn(
        serverName: String,
        serverAddress: String,
        apiKey: String
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            addNewServer(
                serverName = serverName,
                serverAddress = serverAddress,
                token = apiKey
            )
            _isLoading.value = false
        }
    }
}
