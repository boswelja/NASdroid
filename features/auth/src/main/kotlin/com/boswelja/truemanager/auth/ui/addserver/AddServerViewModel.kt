package com.boswelja.truemanager.auth.ui.addserver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.auth.serverstore.AuthenticatedServer
import com.boswelja.truemanager.auth.serverstore.AuthenticatedServersStore
import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.Authorization
import com.boswelja.truemanager.core.api.v2.apikey.ApiKeyV2Api
import com.boswelja.truemanager.core.api.v2.auth.AuthV2Api
import com.boswelja.truemanager.core.api.v2.system.SystemV2Api
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddServerViewModel(
    private val apiStateProvider: ApiStateProvider,
    private val authV2Api: AuthV2Api,
    private val apiKeyV2Api: ApiKeyV2Api,
    private val systemV2Api: SystemV2Api,
    private val authedServersStore: AuthenticatedServersStore,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _events = MutableSharedFlow<Event?>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<Event?> = _events

    fun clearPendingEvent() {
        _events.tryEmit(null)
    }

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
            val isValid = authV2Api.checkPassword(username, password)
            if (isValid) {
                val apiKey = apiKeyV2Api.create("TrueManager for TrueNAS")
                loginWithApiKey(serverName, serverAddress, apiKey)
            } else {
                apiStateProvider.authorization = null
                _events.emit(Event.LoginFailedUsernameOrPasswordInvalid)
            }
            _isLoading.value = false
        }
    }

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
        val uid = systemV2Api.getHostId()

        val actualName = serverName.ifBlank {
            val systemInfo = systemV2Api.getSystemInfo()
            systemInfo.hostInfo.product
        }
        authedServersStore.add(
            AuthenticatedServer(
                uid = uid,
                serverAddress = serverAddress,
                token = apiKey,
                name = actualName
            )
        )
        apiStateProvider.authorization = Authorization.ApiKey(apiKey)
        _events.emit(Event.LoginSuccess)
    }

    enum class Event {
        LoginSuccess,
        LoginFailedKeyInvalid,
        LoginFailedUsernameOrPasswordInvalid,
        LoginFailedServerNotFound
    }
}
