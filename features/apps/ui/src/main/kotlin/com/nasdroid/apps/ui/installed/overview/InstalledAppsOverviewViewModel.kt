package com.nasdroid.apps.ui.installed.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.apps.logic.installed.InstalledApplication
import com.nasdroid.apps.logic.installed.DeleteApp
import com.nasdroid.apps.logic.installed.GetInstalledApps
import com.nasdroid.apps.logic.installed.StartApp
import com.nasdroid.apps.logic.installed.StopApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Exposes data for and receives events from the "installed apps" screen.
 */
class InstalledAppsOverviewViewModel(
    private val getInstalledApps: GetInstalledApps,
    private val startApp: StartApp,
    private val stopApp: StopApp,
    private val deleteApp: DeleteApp,
) : ViewModel() {

    private val _installedApps = MutableStateFlow<List<InstalledApplication>?>(null)

    /**
     * A list of [InstalledApplication]s representing all installed apps.
     */
    val installedApps: StateFlow<List<InstalledApplication>?> = _installedApps

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

    /**
     * Starts an app that is currently stopped.
     */
    fun start(appName: String) {
        viewModelScope.launch {
            startApp(appName)
            refreshSuspending()
        }
    }

    /**
     * Stop an app that is currently active.
     */
    fun stop(appName: String) {
        viewModelScope.launch {
            stopApp(appName)
            refreshSuspending()
        }
    }

    /**
     * Delete an installed app.
     */
    fun delete(appName: String, deleteUnusedImages: Boolean) {
        viewModelScope.launch {
            deleteApp(appName, deleteUnusedImages)
            refreshSuspending()
        }
    }

    private suspend fun refreshSuspending() {
        val installedApps = getInstalledApps()
        _installedApps.emit(installedApps)
    }
}
