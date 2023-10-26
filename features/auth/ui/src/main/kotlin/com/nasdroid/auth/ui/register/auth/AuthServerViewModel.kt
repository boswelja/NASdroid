package com.nasdroid.auth.ui.register.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.auth.logic.auth.TestServerAuthentication
import kotlinx.coroutines.launch

class AuthServerViewModel(
    private val testServerAuthentication: TestServerAuthentication,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val serverAddress = savedStateHandle.get<String>("address")!!

    /**
     * Try to authenticate via username/password. This will verify the credentials, and try to create
     * an API key.
     *
     * @param username The username to authenticate with.
     * @param password The password to authenticate with.
     */
    fun testCredentials(
        username: String,
        password: String
    ) {
        viewModelScope.launch {
            testServerAuthentication(
                serverUrl = serverAddress,
                username = username,
                password = password
            )
        }
    }

    /**
     * Try to authenticate via API key.
     *
     * @param apiKey The API key to try authenticate with.
     */
    fun testCredentials(
        apiKey: String
    ) {
        viewModelScope.launch {
            testServerAuthentication(
                serverUrl = serverAddress,
                apiKey = apiKey
            )
        }
    }
}
