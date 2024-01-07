package com.nasdroid.apps.ui.installed.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.apps.logic.installed.DeleteApp
import com.nasdroid.apps.logic.installed.GetInstalledApps
import com.nasdroid.apps.logic.installed.InstalledAppOverview
import com.nasdroid.apps.logic.installed.StartApp
import com.nasdroid.apps.logic.installed.StopApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
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

    private val _searchTerm = MutableStateFlow("")

    private val _refreshTrigger = MutableSharedFlow<Unit>()

    /**
     * A list of [InstalledAppOverview]s representing all installed apps.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val installedApps: StateFlow<List<InstalledAppOverview>?> = _refreshTrigger
        .flatMapLatest { _searchTerm }
        .flatMapLatest {
            getInstalledApps()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

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
        _refreshTrigger.emit(Unit)
    }
}
