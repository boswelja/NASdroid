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

    /**
     * The name of the application whose data is being displayed. Note the application name is a
     * unique identifier set my the user.
     */
    val appName: StateFlow<String?> = savedStateHandle.getStateFlow("appName", null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _installedAppDetails = appName
        .flatMapLatest {
            flow {
                // We treat "null" as loading, so we want to reset the "loading" state first.
                emit(null)
                emit(it?.let { getInstalledApp(it).getOrNull() })
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            null
        )

    /**
     * The [InstalledAppDetails] for the installed application, or null if data is still loading.
     */
    val appDetails: StateFlow<InstalledAppDetails?> = _installedAppDetails
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    /**
     * Set the name of the app whose details should be viewed.
     */
    fun setAppName(appName: String) {
        savedStateHandle["appName"] = appName
    }
}
