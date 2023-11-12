package com.nasdroid.auth.ui.register.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.auth.logic.manageservers.AddNewServer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Provides data and responds to events for the server authentication screen.
 */
class AuthServerViewModel(
    private val addNewServer: AddNewServer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val serverAddress = savedStateHandle.get<String>("address")!!
    private val _loginState = MutableStateFlow<LoginState?>(null)

    /**
     * The current state of the login process. Can be any of [LoginState], or null. A null value
     * means the user has yet to take any action.
     */
    val loginState: StateFlow<LoginState?> = _loginState

    /**
     * Try to authenticate via username/password. This will verify the credentials, and try to create
     * an API key.
     *
     * @param username The username to authenticate with.
     * @param password The password to authenticate with.
     */
    fun logIn(
        username: String,
        password: String
    ) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = addNewServer(
                serverName = "",
                serverAddress = serverAddress,
                username = username,
                password = password
            )
            _loginState.value = result.fold(
                onSuccess = {
                    LoginState.Success
                },
                onFailure = {
                    LoginState.FailedInvalidCredentials
                }
            )
        }
    }

    /**
     * Try to authenticate via API key.
     *
     * @param apiKey The API key to try authenticate with.
     */
    fun logIn(
        apiKey: String
    ) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = addNewServer(
                serverName = "",
                serverAddress = serverAddress,
                token = apiKey
            )
            _loginState.value = result.fold(
                onSuccess = {
                    LoginState.Success
                },
                onFailure = {
                    LoginState.FailedInvalidCredentials
                }
            )
        }
    }
}

/**
 * Describes all possible states for the login screen.
 */
enum class LoginState {
    Loading,
    Success,
    FailedInvalidCredentials,
}
