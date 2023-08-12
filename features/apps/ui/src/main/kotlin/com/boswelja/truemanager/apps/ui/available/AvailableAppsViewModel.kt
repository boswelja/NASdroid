package com.boswelja.truemanager.apps.ui.available

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.apps.logic.available.AvailableApp
import com.boswelja.truemanager.apps.logic.available.GetAllAvailableApps
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Exposes data for and receives events from the "available apps" screen.
 */
class AvailableAppsViewModel(
    private val getAllAvailableApps: GetAllAvailableApps,
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    private val _availableApps = MutableStateFlow(emptyList<AvailableApp>())

    /**
     * Flows a List of all available applications.
     */
    val availableApps: StateFlow<List<AvailableApp>> = _availableApps

    /**
     * Flows whether data is currently being loaded.
     */
    val isLoading: StateFlow<Boolean> = _loading

    init {
        refresh()
    }

    /**
     * Refresh available applications.
     */
    fun refresh() {
        _loading.value = true
        viewModelScope.launch {
            _availableApps.value = getAllAvailableApps()
            _loading.value = false
        }
    }
}
