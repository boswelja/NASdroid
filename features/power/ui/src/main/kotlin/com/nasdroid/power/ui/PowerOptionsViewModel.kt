package com.nasdroid.power.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.power.logic.RebootError
import com.nasdroid.power.logic.RebootSystem
import com.nasdroid.power.logic.ShutdownError
import com.nasdroid.power.logic.ShutdownSystem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * A ViewModel that exposes power-related controls for the system the app is connected to.
 */
class PowerOptionsViewModel(
    private val shutdownSystem: ShutdownSystem,
    private val rebootSystem: RebootSystem
): ViewModel() {

    private val _powerOptionsState = MutableStateFlow<PowerOptionsState?>(null)

    /**
     * Flows the current state of the power options picker. Can be one of [PowerOptionsState] to
     * indicate in-progress operations, or null to indicate nothing is happening.
     */
    val state: StateFlow<PowerOptionsState?> = _powerOptionsState

    /**
     * Attempts to shut down the system. This will drive [state] accordingly.
     *
     * Attempting to invoke this while [state] is not null is an error.
     */
    fun shutdown() {
        check(_powerOptionsState.value == null) {
            "Attempted to shut down the system while another action was in progress!"
        }
        viewModelScope.launch {
            _powerOptionsState.value = PowerOptionsState.Loading
            val result = shutdownSystem()
            _powerOptionsState.value = when (val error = result.errorOrNull()) {
                is ShutdownError.GenericNetworkError -> PowerOptionsState.ErrorGeneric(
                    technicalDetails = "${error.httpResponseCode}\n\n${error.httpResponseDescription}"
                )
                ShutdownError.Unauthorized -> PowerOptionsState.ErrorUnauthorized
                null -> PowerOptionsState.Success
            }
        }
    }

    /**
     * Attempts to reboot the system. This will drive [state] accordingly.
     *
     * Attempting to invoke this while [state] is not null is an error.
     */
    fun reboot() {
        check(_powerOptionsState.value == null) {
            "Attempted to reboot the system while another action was in progress!"
        }
        viewModelScope.launch {
            _powerOptionsState.value = PowerOptionsState.Loading
            val result = rebootSystem()
            _powerOptionsState.value = when (val error = result.errorOrNull()) {
                is RebootError.GenericNetworkError -> PowerOptionsState.ErrorGeneric(
                    technicalDetails = "${error.httpResponseCode}\n\n${error.httpResponseDescription}"
                )
                RebootError.Unauthorized -> PowerOptionsState.ErrorUnauthorized
                null -> PowerOptionsState.Success
            }
        }
    }

    /**
     * Dismisses any error currently held in [state]. It is an error to call this when there is no
     * error.
     */
    fun dismissError() {
        check(_powerOptionsState.value != null && _powerOptionsState.value != PowerOptionsState.Loading) {
            "Attempted xto dismiss an active error, but there was no active error!"
        }
        _powerOptionsState.value = null
    }
}

/**
 * Encapsulates the state of the power options picker.
 */
sealed interface PowerOptionsState {

    /**
     * Indicates the power options picker is currently loading.
     */
    data object Loading : PowerOptionsState

    /**
     * Indicates the power options picker failed to make a request due to the current user not being
     * authorized.
     */
    data object ErrorUnauthorized : PowerOptionsState

    /**
     * Indicates the power options picker failed to make a request for unknown reasons.
     *
     * @property technicalDetails Technical details of the problem. This can be a multi-line string.
     */
    data class ErrorGeneric(
        val technicalDetails: String
    ) : PowerOptionsState

    /**
     * Indicates the power option picker successfully made a request.
     */
    data object Success : PowerOptionsState
}
