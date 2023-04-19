package com.boswelja.truemanager.auth.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.Authorization
import com.boswelja.truemanager.core.api.v2.apikey.ApiKeyV2Api
import com.boswelja.truemanager.core.api.v2.auth.AuthV2Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val apiStateProvider: ApiStateProvider,
    private val authV2Api: AuthV2Api,
    private val apiKeyV2Api: ApiKeyV2Api,
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
                apiStateProvider.authorization = Authorization.ApiKey(apiKey)
                Log.d("AuthViewModel", "Successfully authenticated")
            } else {
                Log.d("AuthViewModel", "Username, password or server address are invalid")
            }
            _isLoading.value = false
        }
    }

    fun tryLogIn(serverAddress: String, apiKey: String) {
        _isLoading.value = true
        apiStateProvider.serverAddress = serverAddress
        viewModelScope.launch {
            apiStateProvider.authorization = Authorization.ApiKey(apiKey)
            // TODO validate API key
            _isLoading.value = false
        }
    }
}
