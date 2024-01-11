package com.nasdroid.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Describes the state of navigation components on the screen.
 *
 * @property primaryNavigationMode The mode of the primary form of navigation.
 * @property secondaryNavigationMode The mode of the secondary form of navigation.
 */
data class NavigationMode(
    val primaryNavigationMode: PrimaryNavigationMode,
    val secondaryNavigationMode: SecondaryNavigationMode
) {
    companion object {
        /**
         * A convenience function that remembers [NavigationMode].
         */
        @Composable
        fun rememberFromWindowSize(windowSizeClass: WindowSizeClass): NavigationMode {
            return remember(windowSizeClass) {
                calculateFromWindowSize(windowSizeClass)
            }
        }

        /**
         * Calculates a [NavigationMode] from the provided [WindowSizeClass].
         */
        fun calculateFromWindowSize(windowSizeClass: WindowSizeClass): NavigationMode {
            val primaryNavigationMode = if (
                windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded &&
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Expanded
            ) {
                PrimaryNavigationMode.Permanent
            } else {
                PrimaryNavigationMode.Modal
            }
            val secondaryNavigationMode = if (primaryNavigationMode == PrimaryNavigationMode.Permanent) {
                SecondaryNavigationMode.None
            } else if (windowSizeClass.widthSizeClass <= WindowWidthSizeClass.Compact) {
                SecondaryNavigationMode.BottomNavBar
            } else if (
                windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium &&
                windowSizeClass.heightSizeClass >= WindowHeightSizeClass.Medium
            ) {
                SecondaryNavigationMode.StartNavRail
            } else {
                SecondaryNavigationMode.None
            }
            return NavigationMode(primaryNavigationMode, secondaryNavigationMode)
        }
    }
}

/**
 * All possible states for the apps primary mode of navigation.
 */
enum class PrimaryNavigationMode {
    Modal,
    Permanent
}

/**
 * All possible states for the apps secondary mode of navigation.
 *
 * @property providesPrimaryNavigationLauncher Whether the secondary navigation mode provides a way
 * for the user to launch the [PrimaryNavigationMode].
 */
enum class SecondaryNavigationMode(val providesPrimaryNavigationLauncher: Boolean) {
    None(false),
    BottomNavBar(false),
    StartNavRail(true)
}
