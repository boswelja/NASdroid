package com.nasdroid.storage.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.storage.logic.pool.GetPoolOverviews
import com.nasdroid.storage.logic.pool.PoolOverview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Fetches and exposes data used for displaying a list of storage pools.
 */
class StorageOverviewViewModel(
    private val getPoolOverviews: GetPoolOverviews
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)

    /**
     * Whether the ViewModel is currently loading data.
     */
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _pools = MutableStateFlow<List<PoolOverview>>(emptyList())

    /**
     * A list of [PoolOverview]s to display.
     */
    val pools: StateFlow<List<PoolOverview>> = _pools

    init {
        refresh()
    }

    /**
     * Refresh the pool list.
     */
    fun refresh() {
        _isLoading.value = true
        viewModelScope.launch {
            val pools = getPoolOverviews()
            _pools.emit(pools.getOrThrow()) // TODO error handling
            _isLoading.value = false
        }
    }
}
