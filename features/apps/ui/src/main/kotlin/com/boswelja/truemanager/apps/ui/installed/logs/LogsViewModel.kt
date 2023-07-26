package com.boswelja.truemanager.apps.ui.installed.logs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.apps.logic.installed.GetAppLogs
import com.boswelja.truemanager.apps.logic.installed.GetLogOptions
import com.boswelja.truemanager.apps.logic.installed.LogOptions
import com.boswelja.truemanager.apps.logic.installed.SelectedLogOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

/**
 * A ViewModel that provides configuration and logs for a particular app.
 */
class LogsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getLogOptions: GetLogOptions,
    private val getAppLogs: GetAppLogs,
) : ViewModel() {
    private val appName: StateFlow<String?> = savedStateHandle.getStateFlow("appName", null)

    private val _selectedLogOptions = MutableStateFlow<SelectedLogOptions?>(null)

    /**
     * The currently selected log options.
     */
    val selectedLogOptions: StateFlow<SelectedLogOptions?> = _selectedLogOptions

    /**
     * The application logs.
     */
    val logs: StateFlow<List<String>?> = combine(appName, selectedLogOptions) { appName, selectedLogOptions ->
        if (appName == null || selectedLogOptions == null) {
            null
        } else {
            getAppLogs(appName, selectedLogOptions)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    /**
     * Get available log configuration options for the app this ViewModel was created for.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getLogOptions(): Flow<LogOptions> {
        return appName
            .filterNotNull()
            .mapLatest {
                getLogOptions(it)
            }
    }

    fun setSelectedLogOptions(newOptions: SelectedLogOptions) {
        _selectedLogOptions.value = newOptions
    }
}
