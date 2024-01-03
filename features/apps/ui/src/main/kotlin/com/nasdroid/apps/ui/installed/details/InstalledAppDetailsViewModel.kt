package com.nasdroid.apps.ui.installed.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.apps.logic.installed.GetInstalledApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

/**
 * A ViewModel that provides all data and actions necessary for displaying details about an
 * application installed on the system.
 */
class InstalledAppDetailsViewModel(
    private val getInstalledApp: GetInstalledApp,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val appName: StateFlow<String?> = savedStateHandle.getStateFlow("appName", null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _installedAppDetails = appName
        .mapLatest {
            it?.let { getInstalledApp(it).getOrNull() }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val appDetails: StateFlow<AppInfo?> = _installedAppDetails
        .mapLatest {
            it?.let {
                AppInfo(
                    iconUrl = it.iconUrl,
                    name = it.name,
                    appVersion = it.appVersion,
                    chartVersion = it.chartVersion,
                    sources = it.sources,
                    catalogName = it.catalog,
                    trainName = it.train,
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val appNotes: StateFlow<String?> = _installedAppDetails
        .mapLatest { it?.notes }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    fun setAppName(appName: String) {
        savedStateHandle["appName"] = appName
    }
}

data class AppInfo(
    val iconUrl: String,
    val name: String,
    val appVersion: String,
    val chartVersion: String,
    val sources: List<String>,
    val catalogName: String,
    val trainName: String,
)
