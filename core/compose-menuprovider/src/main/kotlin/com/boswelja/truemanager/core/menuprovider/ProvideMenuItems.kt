package com.boswelja.truemanager.core.menuprovider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

/**
 * Provide a number of [MenuItem]s to a [MenuHost].
 */
@Composable
fun ProvideMenuItems(
    vararg menuItems: MenuItem,
    menuHost: MenuHost = LocalMenuHost.current
) {
    DisposableEffect(menuItems) {
        menuHost.addItems(*menuItems)

        onDispose {
            menuHost.removeItems(*menuItems)
        }
    }
}