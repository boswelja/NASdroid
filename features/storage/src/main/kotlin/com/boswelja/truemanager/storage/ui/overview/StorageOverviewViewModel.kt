package com.boswelja.truemanager.storage.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.core.api.v2.pool.Pool
import com.boswelja.truemanager.core.api.v2.pool.PoolV2Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Fetches and exposes data used for displaying a list of storage pools.
 */
class StorageOverviewViewModel(
    private val poolV2Api: PoolV2Api
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)

    /**
     * Whether the ViewModel is currently loading data.
     */
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _pools = MutableStateFlow<List<Pool>>(emptyList())

    /**
     * A list of [Pool]s to display.
     */
    val pools: StateFlow<List<Pool>> = _pools

    init {
        refresh()
    }

    /**
     * Refresh the pool list.
     */
    fun refresh() {
        _isLoading.value = true
        viewModelScope.launch {
            val pools = poolV2Api.getPools()
            _pools.emit(pools)
            _isLoading.value = false
        }
    }
}
