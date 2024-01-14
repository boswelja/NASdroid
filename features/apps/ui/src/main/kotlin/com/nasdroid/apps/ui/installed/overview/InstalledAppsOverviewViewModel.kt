package com.nasdroid.apps.ui.installed.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Exposes data for and receives events from the "installed apps" screen.
 */
class InstalledAppsOverviewViewModel(
    private val getInstalledApps: GetInstalledApps,
    private val startApp: StartApp,
    private val stopApp: StopApp,
) : ViewModel() {

    private val _searchTerm = MutableStateFlow("")

    private val _refreshTrigger = MutableSharedFlow<Unit>()

    private val _loading = MutableStateFlow(true)

    /**
     * Flows whether this ViewModel is currently loading data for [installedApps].
     */
    val isLoading: StateFlow<Boolean> = _loading

    /**
     * A list of [InstalledAppOverview]s representing all installed apps.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val installedApps: StateFlow<List<InstalledAppOverview>?> = _refreshTrigger
        .flatMapLatest { _searchTerm }
        .flatMapLatest {
            getInstalledApps(it)
        }
        .onEach {
            _loading.value = false
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    /**
     * The current text being used to search the app list.
     */
    val searchTerm: StateFlow<String> = _searchTerm

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
        }
    }

    /**
     * Stop an app that is currently active.
     */
    fun stop(appName: String) {
        viewModelScope.launch {
            stopApp(appName)
        }
    }

    /**
     * Sets the term used to search the app list. This will trigger a reload of the app list.
     */
    fun setSearchTerm(searchTerm: String) {
        _loading.value = true
        _searchTerm.value = searchTerm
    }

    private suspend fun refreshSuspending() {
        _loading.value = true
        _refreshTrigger.emit(Unit)
    }
}
