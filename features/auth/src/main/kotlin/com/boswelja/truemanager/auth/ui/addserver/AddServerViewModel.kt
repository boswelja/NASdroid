package com.boswelja.truemanager.auth.ui.addserver

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.auth.serverstore.AuthenticatedServer
import com.boswelja.truemanager.auth.serverstore.AuthenticatedServersStore
import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.Authorization
import com.boswelja.truemanager.core.api.v2.HttpsNotOkException
import com.boswelja.truemanager.core.api.v2.apikey.AllowRule
import com.boswelja.truemanager.core.api.v2.apikey.ApiKeyV2Api
import com.boswelja.truemanager.core.api.v2.auth.AuthV2Api
import com.boswelja.truemanager.core.api.v2.system.SystemV2Api
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * Responsible for exposing data to and handling events from the "add server" UI.
 */
class AddServerViewModel(
    private val apiStateProvider: ApiStateProvider,
    private val authV2Api: AuthV2Api,
    private val apiKeyV2Api: ApiKeyV2Api,
    private val systemV2Api: SystemV2Api,
    private val authedServersStore: AuthenticatedServersStore,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)

    /**
     * Whether the ViewModel is currently loading data. This should be used to switch the UI to a
     * loading state.
     */
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _events = MutableSharedFlow<Event?>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /**
     * Flows [Event]s coming from the ViewModel. If the value is null, there is no event to handle.
     * Note it is up to the collector to clear any existing events via [clearPendingEvent].
     */
    val events: SharedFlow<Event?> = _events

    /**
     * Clears any existing [Event] from [events].
     */
    fun clearPendingEvent() {
        _events.tryEmit(null)
    }

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
        apiStateProvider.serverAddress = serverAddress
        viewModelScope.launch {
            apiStateProvider.authorization = Authorization.Basic(username, password)

            try {
                val isValid = authV2Api.checkPassword(username, password)
                if (isValid) {
                    val apiKey = apiKeyV2Api.create("TrueManager for TrueNAS", listOf(AllowRule("*", "*")))
                    loginWithApiKey(serverName, serverAddress, apiKey.key)
                } else {
                    apiStateProvider.authorization = null
                    _events.emit(Event.LoginFailedUsernameOrPasswordInvalid)
                }
            } catch (_: IOException) {
                _events.emit(Event.LoginFailedServerNotFound)
                apiStateProvider.authorization = null
            }
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
        apiStateProvider.serverAddress = serverAddress
        viewModelScope.launch {
            loginWithApiKey(serverName, serverAddress, apiKey)
            _isLoading.value = false
        }
    }

    private suspend fun loginWithApiKey(
        serverName: String,
        serverAddress: String,
        apiKey: String
    ) {
        apiStateProvider.authorization = Authorization.ApiKey(apiKey)

        try {
            val uid = systemV2Api.getHostId()

            val actualName = serverName.ifBlank {
                val systemInfo = systemV2Api.getSystemInfo()
                systemInfo.systemProduct
            }
            authedServersStore.add(
                AuthenticatedServer(
                    uid = uid,
                    serverAddress = serverAddress,
                    token = apiKey,
                    name = actualName
                )
            )
            _events.emit(Event.LoginSuccess)
        } catch (e: HttpsNotOkException) {
            apiStateProvider.authorization = null
            when (e.code) {
                HttpCodeUnprocessableEntity -> _events.emit(Event.LoginFailedKeyAlreadyExists)
                HttpCodeUnauthorized -> _events.emit(Event.LoginFailedKeyInvalid)
                else -> Log.e(::AddServerViewModel.name, "Unhandled exception $e")
            }
        } catch (_: IOException) {
            _events.emit(Event.LoginFailedServerNotFound)
            apiStateProvider.authorization = null
        }
    }

    /**
     * Describes various events that the ViewModel may emit.
     */
    enum class Event {
        LoginSuccess,
        LoginFailedKeyInvalid,
        LoginFailedKeyAlreadyExists,
        LoginFailedUsernameOrPasswordInvalid,
        LoginFailedServerNotFound,
        LoginFailedNotHttps
    }

    companion object {
        private const val HttpCodeUnprocessableEntity = 422
        private const val HttpCodeUnauthorized = 401
    }
}
