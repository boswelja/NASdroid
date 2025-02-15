package com.nasdroid.dashboard.ui.overview.cpu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.dashboard.logic.dataloading.GetRealtimeStatsError
import com.nasdroid.dashboard.logic.dataloading.cpu.CpuSpecs
import com.nasdroid.dashboard.logic.dataloading.cpu.CpuUsageData
import com.nasdroid.dashboard.logic.dataloading.cpu.GetCpuSpecs
import com.nasdroid.dashboard.logic.dataloading.cpu.GetCpuUsageData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

/**
 * A ViewModel that provides data to the dashboard CPU overview.
 */
class CpuOverviewViewModel(
    private val getCpuSpecs: GetCpuSpecs,
    private val getCpuUsageData: GetCpuUsageData,
) : ViewModel() {

    /**
     * Flows the specifications for the CPU installed in the system. A null value indicates data is
     * still loading.
     */
    val cpuSpecs: StateFlow<Result<CpuSpecs>?> = flow {
        emit(getCpuSpecs())
    }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    /**
     * Flows the utilisation data for the CPU installed in the system. A null value indicates data
     * is still loading.
     */
    val cpuUsageData: StateFlow<StrongResult<CpuUsageData, GetRealtimeStatsError>?> = getCpuUsageData()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )
}
