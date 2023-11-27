package com.nasdroid.apps.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.apps.logic.discover.GetAvailableApps
import com.nasdroid.apps.logic.discover.SortMode
import com.nasdroid.apps.logic.discover.SortedApps
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

class DiscoverAppsViewModel(
    private val getAvailableApps: GetAvailableApps
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    private val _sortMode = MutableStateFlow(SortMode.Category)

    val searchText: StateFlow<String> = _searchText

    val sortMode: StateFlow<SortMode> = _sortMode

    @OptIn(ExperimentalCoroutinesApi::class)
    val availableApps: StateFlow<List<SortedApps>> = combine(searchText, sortMode) { searchText, sortMode ->
        getAvailableApps(searchText, sortMode, emptyList())
    }
        .mapLatest { it.getOrThrow() }
        .catch {
            // TODO handle errors
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )
}
