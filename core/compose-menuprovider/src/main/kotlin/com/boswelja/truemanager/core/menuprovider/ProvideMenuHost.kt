package com.boswelja.truemanager.core.menuprovider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * A convenience function for providing a new MenuHost.
 */
@Composable
fun ProvideMenuHost(
    menuHost: MenuHost,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalMenuHost provides menuHost,
        content = content,
    )
}
