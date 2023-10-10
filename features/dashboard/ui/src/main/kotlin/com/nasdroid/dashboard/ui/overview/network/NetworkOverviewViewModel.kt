package com.nasdroid.dashboard.ui.overview.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.dashboard.logic.dataloading.network.GetNetworkConfiguration
import com.nasdroid.dashboard.logic.dataloading.network.GetNetworkUsageData
import com.nasdroid.dashboard.logic.dataloading.network.NetworkConfiguration
import com.nasdroid.dashboard.logic.dataloading.network.NetworkUsageData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

/**
 * A ViewModel that provides data to the dashboard network overview.
 */
class NetworkOverviewViewModel(
    private val getNetworkConfiguration: GetNetworkConfiguration,
    private val getNetworkUsageData: GetNetworkUsageData,
) : ViewModel() {

    /**
     * Flows the network configuration for the system.
     */
    val networkConfiguration: StateFlow<Result<NetworkConfiguration>?> = flow {
        emit(getNetworkConfiguration())
    }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    /**
     * Flows the utilisation data for the network adapters in the system. A null value indicates
     * data is still loading.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val networkUsageData: StateFlow<Result<NetworkUsageData>?> = networkConfiguration
        .filterNotNull()
        .flatMapLatest {
            it.fold(
                onSuccess = {
                    repeatingFlow(15.seconds) {
                        emit(getNetworkUsageData(it.adapters.map { it.name }))
                    }
                },
                onFailure = {
                    flowOf(Result.failure(it))
                }
            )
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
