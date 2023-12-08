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
import kotlinx.coroutines.flow.update

/**
 * A ViewModel that provides all data and callbacks to provide an app discovery experience to users.
 */
class DiscoverAppsViewModel(
    private val getAvailableApps: GetAvailableApps
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    private val _sortMode = MutableStateFlow(SortMode.Category)
    private val _catalogsFiltered = MutableStateFlow(mapOf("TrueNAS" to true))
    private val _selectedCategories = MutableStateFlow(listOf("Monitoring"))

    /**
     * Flows the test that [availableApps] is currently filtered by.
     */
    val searchText: StateFlow<String> = _searchText

    /**
     * Flows the [SortMode] of [availableApps].
     */
    val sortMode: StateFlow<SortMode> = _sortMode

    /**
     * Flows a Map of catalog names to a Boolean indicating whether they should be included in the
     * list.
     */
    val catalogFilter: StateFlow<Map<String, Boolean>> = _catalogsFiltered

    /**
     * Flows a list of categories the user has selected to filter by.
     */
    val selectedCategories: StateFlow<List<String>> = _selectedCategories

    /**
     * Flows a list of all available apps to be displayed to the user.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val availableApps: StateFlow<List<SortedApps>> = combine(
        searchText,
        sortMode,
        catalogFilter,
        selectedCategories
    ) { searchText, sortMode, catalogFilter, selectedCategories ->
        getAvailableApps(searchText, sortMode, catalogFilter.filterNot { it.value }.keys.toList())
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

    fun setSortMode(sortMode: SortMode) {
        _sortMode.value = sortMode
    }

    fun toggleCatalogFiltered(catalogName: String) {
        _catalogsFiltered.update {
            it.toMutableMap()
                .apply {
                    set(catalogName, !getOrDefault(catalogName, false))
                }
                .toMap()
        }
    }

    fun removeSelectedCategory(category: String) {
        _selectedCategories.update {
            it - category
        }
    }
}
