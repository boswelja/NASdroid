package com.boswelja.truemanager.auth.ui.serverselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.auth.serverstore.AuthenticatedServersStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class SelectServerViewModel(
    authenticatedServersStore: AuthenticatedServersStore
) : ViewModel() {
    val authenticatedServers = authenticatedServersStore.getAll()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )
}
