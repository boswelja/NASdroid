package com.nasdroid.apps.ui.discover.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * Provides all data necessary to display details about a single available app. It is expected that
 * `id`, `catalog` and `train` are provided via [SavedStateHandle]. This is handled automatically by
 * navigation.
 */
class AvailableAppDetailsViewModel(
    savedStateHandle: SavedStateHandle
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
}
