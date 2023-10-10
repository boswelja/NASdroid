package com.nasdroid.dashboard.ui.overview.system

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.dashboard.logic.dataloading.system.GetSystemInformation
import com.nasdroid.dashboard.logic.dataloading.system.SystemInformation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

/**
 * A ViewModel that provides data to the dashboard system overview.
 */
class SystemInformationOverviewViewModel(
    private val getSystemInformation: GetSystemInformation,
) : ViewModel() {

    /**
     * Flows general information for the system. A null value indicates data is still loading.
     */
    val systemInformation: StateFlow<Result<SystemInformation>?> = flow {
        emit(getSystemInformation())
    }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )
}
