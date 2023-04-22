package com.boswelja.truemanager.auth.ui.serverselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.auth.serverstore.AuthenticatedServer
import com.boswelja.truemanager.auth.serverstore.AuthenticatedServersStore
import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.Authorization
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SelectServerViewModel(
    private val apiStateProvider: ApiStateProvider,
    authenticatedServersStore: AuthenticatedServersStore,
) : ViewModel() {
    val authenticatedServers = authenticatedServersStore.getAll()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    fun tryLogIn(server: AuthenticatedServer) {
        _isLoading.value = true
        viewModelScope.launch {
            apiStateProvider.serverAddress = server.serverAddress
            apiStateProvider.authorization = Authorization.ApiKey(server.token)
            // TODO validate token
            _isLoading.value = false
        }
    }
}
