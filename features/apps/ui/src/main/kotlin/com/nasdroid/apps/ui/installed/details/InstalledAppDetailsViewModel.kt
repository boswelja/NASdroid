package com.nasdroid.apps.ui.installed.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.apps.logic.installed.GetInstalledApp
import com.nasdroid.apps.logic.installed.InstalledAppDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
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
        .flatMapLatest {
            flow {
                emit(null)
                emit(it?.let { getInstalledApp(it).getOrNull() })
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            null
        )

    val appDetails: StateFlow<InstalledAppDetails?> = _installedAppDetails
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    fun setAppName(appName: String) {
        savedStateHandle["appName"] = appName
    }
}
