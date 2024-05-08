package com.nasdroid.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.compositionLocalOf

/**
 * Encapsulates all possible modes for NASdroid navigation UI.
 */
enum class NavigationMode {
    NavigationBar,
    NavigationRail,
    PermanentNavigationDrawer
}

/**
 * A [ProvidableCompositionLocal] that provides the current [NavigationMode].
 */
val LocalNavigationMode = compositionLocalOf<NavigationMode> {
    error("Tried accessing LocalNavigationMode before it was set!")
}

/**
 * Holds various default states and utilities related to [NavigationMode].
 */
object NavigationModeDefaults {
    /**
     * Returns the expected [NavigationMode] according to the provided [WindowAdaptiveInfo].
     * Usually used with the [NavigationMode] and related APIs.
     *
     * @param adaptiveInfo the provided [WindowAdaptiveInfo]
     */
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    fun calculateFromAdaptiveInfo(adaptiveInfo: WindowAdaptiveInfo): NavigationMode {
        return with(adaptiveInfo) {
            if (windowPosture.isTabletop ||
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact ||
                windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
            ) {
                NavigationMode.NavigationBar
            } else if (windowSizeClass.widthSizeClass <= WindowWidthSizeClass.Expanded ||
                windowSizeClass.heightSizeClass <= WindowHeightSizeClass.Medium
            ) {
                NavigationMode.NavigationRail
            } else {
                NavigationMode.PermanentNavigationDrawer
            }
        }
    }
}
