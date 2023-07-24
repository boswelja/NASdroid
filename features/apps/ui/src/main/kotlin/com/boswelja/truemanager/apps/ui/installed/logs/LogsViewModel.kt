package com.boswelja.truemanager.apps.ui.installed.logs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.apps.logic.installed.GetLogOptions
import com.boswelja.truemanager.apps.logic.installed.LogOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

/**
 * A ViewModel that provides configuration and logs for a particular app.
 */
class LogsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getLogOptions: GetLogOptions,
) : ViewModel() {
    private val appName: StateFlow<String?> = savedStateHandle.getStateFlow("appName", null)

    /**
     * Available log configuration options for the app this ViewModel was created for.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val logOptions: StateFlow<LogOptions?> = appName
        .filterNotNull()
        .mapLatest {
            getLogOptions(it)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )
}
