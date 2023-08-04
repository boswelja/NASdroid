package com.boswelja.truemanager.auth.ui.serverselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServer
import com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServersStore
import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.Authorization
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Responsible for exposing data to and handling events from the "select server" UI.
 */
class SelectServerViewModel(
    private val apiStateProvider: ApiStateProvider,
    authenticatedServersStore: com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServersStore,
) : ViewModel() {
    /**
     * Flows a list of servers the user has previously authenticated with.
     */
    val authenticatedServers = authenticatedServersStore.getAll()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

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
     * Try log in to the given server.
     */
    fun tryLogIn(server: com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServer) {
        _isLoading.value = true
        viewModelScope.launch {
            apiStateProvider.serverAddress = server.serverAddress
            apiStateProvider.authorization = Authorization.ApiKey(server.token)
            // TODO validate token
            _events.emit(Event.LoginSuccess)
            _isLoading.value = false
        }
    }

    /**
     * Describes various events that the ViewModel may emit.
     */
    enum class Event {
        LoginSuccess,
        LoginFailedTokenInvalid,
        LoginFailedServerNotFound
    }
}
