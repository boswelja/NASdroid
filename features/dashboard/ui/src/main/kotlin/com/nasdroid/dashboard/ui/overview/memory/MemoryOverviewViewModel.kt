package com.nasdroid.dashboard.ui.overview.memory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.dashboard.logic.dataloading.memory.GetMemorySpecs
import com.nasdroid.dashboard.logic.dataloading.memory.GetMemoryUsageData
import com.nasdroid.dashboard.logic.dataloading.memory.MemorySpecs
import com.nasdroid.dashboard.logic.dataloading.memory.MemoryUsageData
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
 * A ViewModel that provides data to the dashboard memory overview.
 */
class MemoryOverviewViewModel(
    private val getMemorySpecs: GetMemorySpecs,
    private val getMemoryUsageData: GetMemoryUsageData,
) : ViewModel() {

    /**
     * Flows the specifications for the memory installed in the system. A null value indicates data
     * is still loading.
     */
    val memorySpecs: StateFlow<Result<MemorySpecs>?> = flow {
        emit(getMemorySpecs())
    }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    /**
     * Flows the utilisation data for the memory installed in the system. A null value indicates
     * data is still loading.
     */
    val memoryUsageData: StateFlow<Result<MemoryUsageData>?> = repeatingFlow(15.seconds) {
        emit(getMemoryUsageData())
    }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    private fun <T> repeatingFlow(interval: Duration, block: suspend FlowCollector<T>.() -> Unit): Flow<T> {
        return flow {
            while (coroutineContext.isActive) {
                val executionTime = measureTime {
                    block()
                }
                delay((interval - executionTime).coerceAtLeast(Duration.ZERO))
            }
        }
    }
}
