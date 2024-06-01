package com.nasdroid.navigation.drawer

import androidx.compose.runtime.compositionLocalOf

/**
 * Holds various functions for controlling the state of the modal navigation drawer provided by
 * [NavigationDrawerLayout], if possible.
 *
 * @property openDrawer Opens the navigation drawer. This will suspend until the animation completes.
 */
data class ModalDrawerController(
    val openDrawer: suspend () -> Unit,
)

/**
 * Provides a [ModalDrawerController]. The default value throws an exception if there is no drawer,
 * and a function is called.
 */
internal val LocalModalDrawerController = compositionLocalOf {
    ModalDrawerController { error("No drawer attached!") }
}
