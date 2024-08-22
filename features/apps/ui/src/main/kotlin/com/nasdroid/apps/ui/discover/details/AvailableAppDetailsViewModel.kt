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

    /**
     * Flows the latest [State] of the screen.
     */
    val state: StateFlow<State> = _state

    /**
     * Flows an [AvailableAppDetails] for the app that should be displayed.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val appDetails: StateFlow<AvailableAppDetails?> = _refreshEvent
        .mapLatest { _ ->
            _state.value = State.Loading
            getAvailableAppDetails(appId, catalog, train).fold(
                onSuccess = { details ->
                    _state.value = State.Success
                    details
                },
                onFailure = { error ->
                    _state.value = when (error) {
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

    /**
     * Flows a list of [AvailableApp]s that are similar to the app that this ViewModel is providing
     * details for.
     */
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

    /**
     * Triggers a refresh of [appDetails] and [similarApps].
     */
    fun refresh() {
        viewModelScope.launch {
            _refreshEvent.emit(Unit)
        }
    }

    /**
     * Encapsulates possible states of the ViewModel.
     */
    sealed interface State {

        /**
         * Indicates that data is being loaded.
         */
        data object Loading : State

        /**
         * Indicates that data was successfully loaded.
         */
        data object Success : State

        /**
         * Encapsulates all possible failures that can occur during the loading process.
         */
        sealed interface Error : State {

            /**
             * Indicates that there was no matching app to be found on the server.
             */
            data object AppNotFound : Error
        }
    }
}
