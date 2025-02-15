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
    private val _registerProblem = MutableStateFlow<RegisterProblem?>(null)

    /**
     * The current state of the login process. Can be any of [RegisterState], or null. A null value
     * means the user has yet to take any action.
     */
    val registerState: StateFlow<RegisterState?> = _registerState

    /**
     * Indicates any problems with the users inputted data. Can be any of [RegisterProblem], or null
     * if the user hasn't tried to submit anything.
     */
    val registerProblem: StateFlow<RegisterProblem?> = _registerProblem

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
     * @param createApiKey Whether an API key should be created.
     */
    fun tryRegisterServer(
        serverAddress: String,
        username: String,
        password: String,
        createApiKey: Boolean,
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
                createApiKey = createApiKey
            )
            result.fold(
                onSuccess = {
                    _registerState.value = RegisterState.Success
                },
                onFailure = {
                    _registerProblem.value = when (it) {
                        AddServerError.DuplicateEntry -> GenericError.DuplicateEntry
                        AddServerError.InvalidCredentials -> AuthError.InvalidCredentials
                        AddServerError.ServerNotFound -> AddressError.ServerNotFound
                        is AddServerError.InvalidAddress -> AddressError.AddressInvalid
                        AddServerError.FailedToCreateApiKey -> AuthError.FailedToCreateApiKey
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
            result.fold(
                onSuccess = {
                    _registerState.value = RegisterState.Success
                },
                onFailure = {
                    _registerProblem.value = when (it) {
                        AddServerError.DuplicateEntry -> GenericError.DuplicateEntry
                        AddServerError.InvalidCredentials -> AuthError.InvalidCredentials
                        AddServerError.ServerNotFound -> AddressError.ServerNotFound
                        is AddServerError.InvalidAddress -> AddressError.AddressInvalid
                        AddServerError.FailedToCreateApiKey -> AuthError.FailedToCreateApiKey
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
     * Indicates a server is currently being registered. The user is unable to perform actions while
     * this state is active.
     */
    data object Loading : RegisterState

    /**
     * Indicates registering a server was successful. The server has been registered, and the user
     * should be taken away from the register flow.
     */
    data object Success : RegisterState
}

sealed interface RegisterProblem

/**
 * Encapsulates all possible authentication-related errors.
 */
sealed interface AuthError : RegisterProblem {

    /**
     * Indicates the credentials provided were invalid.
     */
    data object InvalidCredentials : AuthError

    /**
     * Indicates the credentials were correct, but an API key could not be created. This could
     * be caused by an existing API key with the same name.
     */
    data object FailedToCreateApiKey : AuthError
}

/**
 * Encapsulates all possible server address-related errors.
 */
sealed interface AddressError : RegisterProblem {

    /**
     * Indicates the server at the provided address could not be found.
     */
    data object ServerNotFound : AddressError

    /**
     * Indicates that the text entered was not a valid address.
     */
    data object AddressInvalid : AddressError
}

/**
 * Encapsulates all other errors that cannot be attributed to any single user input step.
 */
sealed interface GenericError : RegisterProblem {

    /**
     * Indicates that a server is already registered that matches all the information provided.
     */
    data object DuplicateEntry : GenericError
}