package com.nasdroid.navigation

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.compositionLocalOf
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

/**
 * Describes the state of navigation components on the screen.
 *
 * @property primaryNavigationMode The mode of the primary form of navigation.
 * @property secondaryNavigationMode The mode of the secondary form of navigation.
 */
data class NavigationMode(
    val primaryNavigationMode: PrimaryNavigationMode,
    val secondaryNavigationMode: SecondaryNavigationMode
)

/**
 * All possible states for the apps primary mode of navigation.
 */
enum class PrimaryNavigationMode {
    Modal,
    Permanent
}

/**
 * All possible states for the apps secondary mode of navigation.
 */
enum class SecondaryNavigationMode {
    None,
    Bar,
    Rail
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
    fun calculateFromAdaptiveInfo(adaptiveInfo: WindowAdaptiveInfo): NavigationMode {
        return with(adaptiveInfo) {
            if (windowPosture.isTabletop ||
                windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT ||
                windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
            ) {
                NavigationMode(PrimaryNavigationMode.Modal, SecondaryNavigationMode.Bar)
            } else if (windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.EXPANDED) {
                NavigationMode(PrimaryNavigationMode.Modal, SecondaryNavigationMode.Rail)
            } else {
                NavigationMode(PrimaryNavigationMode.Permanent, SecondaryNavigationMode.None)
            }
        }
    }
}
