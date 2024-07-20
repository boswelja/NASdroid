package com.nasdroid.apps.ui.discover.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.apps.logic.discover.AvailableApp
import com.nasdroid.apps.logic.discover.AvailableAppDetails
import com.nasdroid.apps.logic.discover.AvailableAppDetailsError
import com.nasdroid.apps.logic.discover.GetAvailableAppDetails
import com.nasdroid.apps.logic.discover.GetSimilarApps
import com.nasdroid.core.strongresult.fold
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Provides all data necessary to display details about a single available app. It is expected that
 * `id`, `catalog` and `train` are provided via [SavedStateHandle]. This is handled automatically by
 * navigation.
 */
class AvailableAppDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getAvailableAppDetails: GetAvailableAppDetails,
    private val getSimilarApps: GetSimilarApps,
) : ViewModel() {
    private val catalog: String = requireNotNull(savedStateHandle["catalog"]) {
        "Argument `catalog` is required to display available app details"
    }
    private val train: String = requireNotNull(savedStateHandle["train"]) {
        "Argument `train` is required to display available app details"
    }
    private val appId: String = requireNotNull(savedStateHandle["id"]) {
        "Argument `id` is required to display available app details"
    }

    private val _refreshEvent = MutableSharedFlow<Unit>()

    private val _state = MutableStateFlow<State>(State.Loading)

    val state: StateFlow<State> = _state

    @OptIn(ExperimentalCoroutinesApi::class)
    val appDetails: StateFlow<AvailableAppDetails?> = _refreshEvent
        .mapLatest {
            _state.value = State.Loading
            getAvailableAppDetails(appId, catalog, train).fold(
                onSuccess = {
                    _state.value = State.Success
                    it
                },
                onFailure = {
                    _state.value = when (it) {
                        AvailableAppDetailsError.AppNotFound -> State.Error.AppNotFound
                    }
                    null
                }
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val similarApps: StateFlow<List<AvailableApp>> = _refreshEvent
        .mapLatest {
            getSimilarApps(appId, catalog, train).getOrNull().orEmpty()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _refreshEvent.emit(Unit)
        }
    }

    sealed interface State {
        data object Loading : State
        data object Success : State

        sealed interface Error : State {
            data object AppNotFound : Error
        }
    }
}
