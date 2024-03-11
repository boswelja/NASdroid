package com.nasdroid.power.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.power.logic.RebootSystem
import com.nasdroid.power.logic.ShutdownSystem
import kotlinx.coroutines.launch

/**
 * A ViewModel that exposes power-related controls for the system the app is connected to.
 */
class PowerOptionsViewModel(
    private val shutdownSystem: ShutdownSystem,
    private val rebootSystem: RebootSystem
): ViewModel() {

    /**
     * Attempts to shut down the system.
     */
    fun shutdown() {
        viewModelScope.launch {
            shutdownSystem()
        }
    }

    /**
     * Attempts to reboot the system.
     */
    fun reboot() {
        viewModelScope.launch {
            rebootSystem()
        }
    }
}