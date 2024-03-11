package com.nasdroid.power.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.power.logic.RebootSystem
import com.nasdroid.power.logic.ShutdownSystem
import kotlinx.coroutines.launch

class PowerOptionsViewModel(
    private val shutdownSystem: ShutdownSystem,
    private val rebootSystem: RebootSystem
): ViewModel() {
    fun shutdown() {
        viewModelScope.launch {
            shutdownSystem()
        }
    }

    fun reboot() {
        viewModelScope.launch {
            rebootSystem()
        }
    }
}