package com.nasdroid.apps.ui.installed.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.apps.logic.installed.GetInstalledApp
import com.nasdroid.apps.logic.installed.InstalledAppDetails
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
    val appDetails: StateFlow<InstalledAppDetails?> = appName
        .mapLatest { appName ->
            appName?.let { getInstalledApp(it).getOrNull() } // TODO handle errors
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    fun setAppName(appName: String) {
        savedStateHandle["appName"] = appName
    }
}
