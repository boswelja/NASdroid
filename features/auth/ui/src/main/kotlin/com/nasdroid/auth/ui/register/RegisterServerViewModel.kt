package com.nasdroid.auth.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.auth.logic.manageservers.AddNewServer
import com.nasdroid.auth.logic.manageservers.AddServerError
import com.nasdroid.core.strongresult.fold
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Provides data and responds to events for the server authentication screen.
 */
class RegisterServerViewModel(
    private val addNewServer: AddNewServer,
) : ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState?>(null)

    /**
     * The current state of the login process. Can be any of [RegisterState], or null. A null value
     * means the user has yet to take any action.
     */
    val registerState: StateFlow<RegisterState?> = _registerState

    /**
     * Resets any possible pending state held by [registerState]. If an error state is currently
     * held, it will be reset to null.
     */
    fun clearPendingState() {
        require(registerState.value != RegisterState.Loading) { "Tried to reset registerState while it was loading!" }
        _registerState.value = null
    }

    /**
     * Try to register a new server.
     *
     * @param serverAddress The address of the server to connect to.
     * @param username The username to authenticate with.
     * @param password The password to authenticate with.
     */
    fun tryRegisterServer(
        serverAddress: String,
        username: String,
        password: String
    ) {
        require(registerState.value != RegisterState.Loading) {
            "Tried to register a new server while registerState was loading!"
        }
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            val result = addNewServer(
                serverName = "",
                serverAddress = serverAddress,
                username = username,
                password = password,
                createApiKey = true
            )
            _registerState.value = result.fold(
                onSuccess = {
                    RegisterState.Success
                },
                onFailure = {
                    when (it) {
                        AddServerError.DuplicateEntry -> RegisterState.GenericError.DuplicateEntry
                        AddServerError.InvalidCredentials -> RegisterState.AuthError.InvalidCredentials
                        AddServerError.ServerNotFound -> RegisterState.AddressError.ServerNotFound
                        is AddServerError.InvalidAddress -> TODO()
                    }
                }
            )
        }
    }

    /**
     * Try to register a new server.
     *
     * @param serverAddress The address of the server to connect to.
     * @param apiKey The API key to try authenticate with.
     */
    fun tryRegisterServer(
        serverAddress: String,
        apiKey: String
    ) {
        require(registerState.value != RegisterState.Loading) {
            "Tried to register a new server while registerState was loading!"
        }
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            val result = addNewServer(
                serverName = "",
                serverAddress = serverAddress,
                token = apiKey
            )
            _registerState.value = result.fold(
                onSuccess = {
                    RegisterState.Success
                },
                onFailure = {
                    when (it) {
                        AddServerError.DuplicateEntry -> RegisterState.GenericError.DuplicateEntry
                        AddServerError.InvalidCredentials -> RegisterState.AuthError.InvalidCredentials
                        AddServerError.ServerNotFound -> RegisterState.AddressError.ServerNotFound
                        is AddServerError.InvalidAddress -> TODO()
                    }
                }
            )
        }
    }
}

/**
 * Encapsulates all possible states for the register server screen.
 */
sealed interface RegisterState {

    /**
     * Indicates a server is currently being registered.
     */
    data object Loading : RegisterState

    /**
     * Indicates registering a server was successful.
     */
    data object Success : RegisterState

    /**
     * Encapsulates all possible authentication-related errors.
     */
    sealed interface AuthError : RegisterState {

        /**
         * Indicates the credentials provided were invalid.
         */
        data object InvalidCredentials : AuthError
    }

    /**
     * Encapsulates all possible server address-related errors.
     */
    sealed interface AddressError : RegisterState {

        /**
         * Indicates the server at the provided address could not be found.
         */
        data object ServerNotFound : AddressError
    }

    /**
     * Encapsulates all other errors that cannot be attributed to any single user input step.
     */
    sealed interface GenericError : RegisterState {

        /**
         * Indicates that a server is already registered that matches all the information provided.
         */
        data object DuplicateEntry : GenericError
    }
}
