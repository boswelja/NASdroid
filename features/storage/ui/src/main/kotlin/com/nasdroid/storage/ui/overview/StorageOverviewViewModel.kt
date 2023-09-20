package com.nasdroid.storage.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.api.v2.pool.Pool
import com.nasdroid.storage.logic.pool.GetPools
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Fetches and exposes data used for displaying a list of storage pools.
 */
class StorageOverviewViewModel(
    private val getPools: GetPools
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
            val pools = getPools()
            _pools.emit(pools)
            _isLoading.value = false
        }
    }
}
