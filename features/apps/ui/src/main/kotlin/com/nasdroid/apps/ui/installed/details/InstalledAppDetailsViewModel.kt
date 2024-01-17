package com.nasdroid.apps.ui.installed.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.apps.logic.installed.DeleteApp
import com.nasdroid.apps.logic.installed.GetInstalledApp
import com.nasdroid.apps.logic.installed.GetRollbackOptions
import com.nasdroid.apps.logic.installed.InstalledAppDetails
import com.nasdroid.apps.logic.installed.RollbackApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * A ViewModel that provides all data and actions necessary for displaying details about an
 * application installed on the system.
 */
class InstalledAppDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getInstalledApp: GetInstalledApp,
    private val deleteApp: DeleteApp,
    private val getRollbackOptions: GetRollbackOptions,
    private val rollbackApp: RollbackApp,
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
     * The options available to the user when initiating a rollback of the installed app.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val rollbackOptions = appName
        .flatMapLatest {
            flow {
                // We treat "null" as loading, so we want to reset the "loading" state first.
                emit(null)
                emit(it?.let { getRollbackOptions(it) })
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
    fun setAppName(appName: String?) {
        savedStateHandle["appName"] = appName
    }

    /**
     * Attempts to delete the app whose name matches [appName].
     *
     * @param deleteUnusedImages Whether container images that are unused after the operation should
     * be deleted.
     */
    fun tryDeleteApp(deleteUnusedImages: Boolean) {
        viewModelScope.launch {
            deleteApp(checkNotNull(appName.value), deleteUnusedImages)
        }
    }

    /**
     * Attempts to roll back the installed version of the app whose name matches [appName].
     */
    fun tryRollBackApp(targetVersion: String, rollbackSnapshots: Boolean) {
        viewModelScope.launch {
            rollbackApp(checkNotNull(appName.value), targetVersion, rollbackSnapshots)
        }
    }
}
