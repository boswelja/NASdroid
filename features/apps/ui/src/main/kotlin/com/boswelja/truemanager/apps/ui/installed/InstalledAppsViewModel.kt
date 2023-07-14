package com.boswelja.truemanager.apps.ui.installed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.apps.logic.installed.ApplicationOverview
import com.boswelja.truemanager.apps.logic.installed.GetInstalledApps
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InstalledAppsViewModel(
    private val getInstalledApps: GetInstalledApps
) : ViewModel() {

    private val _installedApps = MutableStateFlow<List<ApplicationOverview>?>(null)
    val installedApps: StateFlow<List<ApplicationOverview>?> = _installedApps

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val installedApps = getInstalledApps()
            _installedApps.emit(installedApps)
        }
    }
}
