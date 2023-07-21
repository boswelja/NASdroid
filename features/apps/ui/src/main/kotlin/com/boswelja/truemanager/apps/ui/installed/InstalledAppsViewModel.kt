package com.boswelja.truemanager.apps.ui.installed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.apps.logic.installed.ApplicationOverview
import com.boswelja.truemanager.apps.logic.installed.GetInstalledApps
import com.boswelja.truemanager.apps.logic.installed.StartApp
import com.boswelja.truemanager.apps.logic.installed.StopApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Exposes data for and receives events from the "installed apps" screen.
 */
class InstalledAppsViewModel(
    private val getInstalledApps: GetInstalledApps,
    private val startApp: StartApp,
    private val stopApp: StopApp,
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
            refreshSuspending()
        }
    }

    fun start(appName: String) {
        viewModelScope.launch {
            startApp(appName)
            refreshSuspending()
        }
    }

    fun stop(appName: String) {
        viewModelScope.launch {
            stopApp(appName)
            refreshSuspending()
        }
    }

    private suspend fun refreshSuspending() {
        val installedApps = getInstalledApps()
        _installedApps.emit(installedApps)
    }
}
