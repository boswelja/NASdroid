package com.nasdroid.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/**
 * Displays either a [PermanentNavigationDrawer], or a [ModalNavigationDrawer], depending on the
 * state of [navigationMode].
 */
@Composable
fun NavigationDrawerLayout(
    drawerContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    navigationMode: NavigationMode = LocalNavigationMode.current,
    content: @Composable () -> Unit
) {
    if (navigationMode == NavigationMode.PermanentNavigationDrawer) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(
                    content = drawerContent
                )
            },
            content = content,
            modifier = modifier
        )
    } else {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val controller = remember(drawerState) {
            ModalDrawerController(
                openDrawer = drawerState::open
            )
        }
        CompositionLocalProvider(
            LocalModalDrawerController provides controller
        ) {
            ModalNavigationDrawer(
                drawerContent = {
                    ModalDrawerSheet(
                        drawerState = drawerState,
                        content = drawerContent
                    )
                },
                drawerState = drawerState,
                content = content,
                modifier = modifier
            )
        }
    }
}

/**
 * Holds various functions for controlling the state of the modal navigation drawer provided by
 * [NavigationDrawerLayout], if possible.
 *
 * @property openDrawer Opens the navigation drawer. This will suspend until the animation completes.
 */
internal data class ModalDrawerController(
    val openDrawer: suspend () -> Unit,
)

/**
 * Provides a [ModalDrawerController]. The default value throws an exception if there is no drawer,
 * and a function is called.
 */
internal val LocalModalDrawerController = compositionLocalOf {
    ModalDrawerController { error("No drawer attached!") }
}
