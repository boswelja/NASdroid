package com.boswelja.truemanager.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.auth.AuthV2Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class AuthViewModel(
    private val apiStateProvider: ApiStateProvider,
    private val authV2Api: AuthV2Api
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun tryLogIn(serverAddress: String, username: String, password: String) {
        _isLoading.value = true
        apiStateProvider.serverAddress = serverAddress
        viewModelScope.launch {
            val isValid = authV2Api.checkPassword(username, password)
            if (isValid) {
                val token = authV2Api.generateToken(username, password, 600.seconds, false)
                apiStateProvider.sessionToken = token
                Log.d("AuthViewModel", "Successfully authenticated")
            } else {
                Log.d("AuthViewModel", "Username, password or server address are invalid")
            }
            _isLoading.value = false
        }
    }
}
