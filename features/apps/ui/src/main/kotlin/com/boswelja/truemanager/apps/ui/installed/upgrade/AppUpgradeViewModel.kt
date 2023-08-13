package com.boswelja.truemanager.apps.ui.installed.upgrade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class AppUpgradeViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val appName = savedStateHandle.getStateFlow<String?>("appName", null)
}
