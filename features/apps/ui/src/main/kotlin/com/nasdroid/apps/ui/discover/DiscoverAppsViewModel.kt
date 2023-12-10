package com.nasdroid.apps.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.apps.logic.discover.GetAvailableApps
import com.nasdroid.apps.logic.discover.GetAvailableCatalogs
import com.nasdroid.apps.logic.discover.GetAvailableCategories
import com.nasdroid.apps.logic.discover.SortMode
import com.nasdroid.apps.logic.discover.SortedApps
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * A ViewModel that provides all data and callbacks to provide an app discovery experience to users.
 */
class DiscoverAppsViewModel(
    private val getAvailableApps: GetAvailableApps,
    private val getAvailableCatalogs: GetAvailableCatalogs,
    private val getAvailableCategories: GetAvailableCategories,
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    private val _sortMode = MutableStateFlow(SortMode.Category)
    private val _catalogsFiltered = MutableStateFlow(emptyMap<String, Boolean>())
    private val _selectedCategories = MutableStateFlow(emptyList<String>())
    private val _availableCategories = MutableStateFlow(emptyList<String>())
    private val _isFilterLoading = MutableStateFlow(false)
    private val _isAppListLoading = MutableStateFlow(false)

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
     * Flows a list of all categories exposed by catalogs on the system.
     */
    val availableCategories: StateFlow<List<String>> = _availableCategories

    /**
     * Flows whether the filter is loading data.
     */
    val isFilterLoading: StateFlow<Boolean> = _isFilterLoading

    /**
     * Flows whether the app list is loading data.
     */
    val isAppListLoading: StateFlow<Boolean> = _isAppListLoading

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
        _isAppListLoading.value = true
        val apps = getAvailableApps(
            searchQuery = searchText,
            sortMode = sortMode,
            filteredCatalogs = catalogFilter.filter { it.value }.keys.toList(),
            filteredCategories = selectedCategories
        )
        _isAppListLoading.value = false
        apps
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

    init {
        refreshFilterData()
    }

    /**
     * Sets how [availableApps] should be sorted.
     */
    fun setSortMode(sortMode: SortMode) {
        _sortMode.value = sortMode
    }

    /**
     * Toggles whether apps belonging to [catalogName] should be included in [availableApps]. Note
     * that toggling all catalogs off is the same as toggling them all on.
     */
    fun toggleCatalogFiltered(catalogName: String) {
        _catalogsFiltered.update {
            it.toMutableMap()
                .apply {
                    set(catalogName, !getOrDefault(catalogName, false))
                }
                .toMap()
        }
    }

    /**
     * Removes [category] from the list of categories that should be excluded from [availableApps].
     * Note that removing all categories is the same as adding all categories.
     */
    fun removeSelectedCategory(category: String) {
        _selectedCategories.update {
            it - category
        }
    }

    /**
     * Adds [category] to the list of categories that should be included in [availableApps]. Note
     * that removing all categories is the same as adding all categories.
     */
    fun addCategoryToFilter(category: String) {
        _selectedCategories.update {
            it + category
        }
    }

    private fun refreshFilterData() {
        viewModelScope.launch {
            _isFilterLoading.value = true
            val catalogUpdater = async {
                val catalogs = getAvailableCatalogs()
                // TODO handle errors
                catalogs.getOrNull()?.let {
                    _catalogsFiltered.value = it.associateWith { true }
                }
            }
            val categoriesUpdater = async {
                val categories = getAvailableCategories()
                // TODO handle errors
                categories.getOrNull()?.let {
                    _availableCategories.value = it
                }
            }
            awaitAll(catalogUpdater, categoriesUpdater)
            _isFilterLoading.value = false
        }
    }
}
