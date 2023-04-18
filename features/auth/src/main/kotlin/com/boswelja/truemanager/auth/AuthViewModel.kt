package com.boswelja.truemanager.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.core.api.v2.auth.AuthV2Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authV2Api: AuthV2Api
) : ViewModel() {

    val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun tryLogIn(serverAddress: String, username: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            authV2Api.checkPassword(username, password)
            _isLoading.value = false
        }
    }
}
