package com.boswelja.truemanager.apps.ui.installed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.apps.logic.installed.ApplicationOverview
import com.boswelja.truemanager.apps.logic.installed.GetInstalledApps
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Exposes data for and receives events from the "installed apps" screen.
 */
class InstalledAppsViewModel(
    private val getInstalledApps: GetInstalledApps
) : ViewModel() {

    private val _installedApps = MutableStateFlow<List<ApplicationOverview>?>(null)

    /**
     * A list of [ApplicationOverview]s representing all installed apps.
     */
    val installedApps: StateFlow<List<ApplicationOverview>?> = _installedApps

    init {
        refresh()
    }

    /**
     * Refresh the data in this ViewModel.
     */
    fun refresh() {
        viewModelScope.launch {
            val installedApps = getInstalledApps()
            _installedApps.emit(installedApps)
        }
    }
}
