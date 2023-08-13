package com.boswelja.truemanager.apps.ui.installed.upgrade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.apps.logic.installed.GetInstalledAppOverview
import com.boswelja.truemanager.apps.logic.installed.GetUpgradeDetails
import com.boswelja.truemanager.apps.logic.installed.InstalledAppOverview
import com.boswelja.truemanager.apps.logic.installed.UpgradeDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

class AppUpgradeViewModel(
    savedStateHandle: SavedStateHandle,
    private val getUpgradeDetails: GetUpgradeDetails,
    private val getInstalledAppOverview: GetInstalledAppOverview,
): ViewModel() {
    private val appName = savedStateHandle.getStateFlow<String?>("appName", null)

    private val targetVersion = MutableStateFlow<String?>(null)
    private val _loading = MutableStateFlow(false)

    val loading: StateFlow<Boolean> = _loading

    val upgradeDetails: StateFlow<UpgradeDetails?> =
        combine(appName, targetVersion) { (appName, targetVersion) ->
            _loading.value = true
            val details = appName?.let {
                getUpgradeDetails(appName, targetVersion)
            }
            _loading.value = false
            details
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    val appOverview: StateFlow<InstalledAppOverview?> = appName
        .filterNotNull()
        .mapLatest {
            getInstalledAppOverview(it)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    fun setTargetVersion(targetVersion: String) {
        this.targetVersion.value = targetVersion
    }
}
