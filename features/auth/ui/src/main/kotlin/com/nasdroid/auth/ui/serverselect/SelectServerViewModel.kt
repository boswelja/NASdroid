package com.nasdroid.auth.ui.serverselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.auth.logic.auth.LogIn
import com.nasdroid.auth.logic.manageservers.GetAllServers
import com.nasdroid.auth.logic.Server
import com.nasdroid.auth.logic.auth.LoginError
import com.nasdroid.core.strongresult.fold
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Responsible for exposing data to and handling events from the "select server" UI.
 */
class SelectServerViewModel(
    private val logIn: LogIn,
    getAllServers: GetAllServers,
) : ViewModel() {

    /**
     * Flows a list of servers the user has previously authenticated with.
     */
    val authenticatedServers = getAllServers()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private val _loginState = MutableStateFlow<LoginState?>(null)

    /**
     * Flows [LoginState]s coming from the ViewModel. If the value is null, there is no event to handle.
     * Note it is up to the collector to clear any existing events via [clearPendingEvent].
     */
    val loginState: StateFlow<LoginState?> = _loginState

    /**
     * Clears any existing [LoginState] from [loginState].
     */
    fun clearPendingEvent() {
        _loginState.tryEmit(null)
    }

    /**
     * Try log in to the given server.
     */
    fun tryLogIn(server: Server) {
        require(_loginState.value != LoginState.Loading) {
            "Tried to start a second login request, but the ViewModel was busy already!"
        }
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val result = logIn(server)
            val newState = result.fold(
                onSuccess = { LoginState.LoginSuccess },
                onFailure = {
                    when (it) {
                        LoginError.InvalidCredentials -> LoginState.Error.CredentialsInvalid
                        LoginError.ServerUnreachable -> LoginState.Error.ServerUnreachable
                        LoginError.Unknown -> LoginState.Error.Generic
                    }
                }
            )
            _loginState.emit(newState)
        }
    }
}

/**
 * Describes various events that the ViewModel may emit.
 */
sealed interface LoginState {

    /**
     * The login screen is currently processing a request.
     */
    data object Loading : LoginState

    /**
     * The login screen has successfully authenticated with a server.
     */
    data object LoginSuccess : LoginState

    /**
     * An error occurred during the login process.
     */
    sealed interface Error : LoginState {

        /**
         * A login request was made with the server, but we were unauthorized.
         */
        data object CredentialsInvalid : Error

        /**
         * A login request could not be made to the server because it was not found.
         */
        data object ServerUnreachable : Error

        /**
         * The login request failed for an undefined reason.
         */
        data object Generic : Error
    }
}
