package com.boswelja.truemanager.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.auth.serverstore.AuthenticatedServersStore
import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.Authorization
import com.boswelja.truemanager.core.api.v2.apikey.ApiKeyV2Api
import com.boswelja.truemanager.core.api.v2.auth.AuthV2Api
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val apiStateProvider: ApiStateProvider,
    private val authV2Api: AuthV2Api,
    private val apiKeyV2Api: ApiKeyV2Api,
    private val authedServersStore: AuthenticatedServersStore,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun tryLogIn(serverAddress: String, username: String, password: String) {
        _isLoading.value = true
        apiStateProvider.serverAddress = serverAddress
        viewModelScope.launch {
            val isValid = authV2Api.checkPassword(username, password)
            if (isValid) {
                apiStateProvider.authorization = Authorization.Basic(username, password)
                val apiKey = apiKeyV2Api.create("TrueManager for TrueNAS")
                authedServersStore.add(serverAddress, apiKey)
                apiStateProvider.authorization = Authorization.ApiKey(apiKey)
            }
            _isLoading.value = false
        }
    }

    fun tryLogIn(serverAddress: String, apiKey: String) {
        _isLoading.value = true
        apiStateProvider.serverAddress = serverAddress
        viewModelScope.launch {
            //authedServersStore.add(serverAddress, apiKey)
            delay(500)
            apiStateProvider.authorization = Authorization.ApiKey(apiKey)
            // TODO validate API key
            _isLoading.value = false
        }
    }
}
