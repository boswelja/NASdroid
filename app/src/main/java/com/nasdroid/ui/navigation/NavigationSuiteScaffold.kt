package com.nasdroid.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A small wrapper around [Scaffold] that provides slots for various navigation components.
 *
 * @param modifier [Modifier].
 * @param topBar A slot at the top of the scaffold to host a navigation bar.
 * @param bottomBar A slot at the bottom of the scaffold to host a navigation bar.
 * @param startBar A slot at the start of the scaffold to host a navigation bar.
 * @param content The content to fill the remaining space on the screen. Note you must apply the
 * provided PaddingValues.
 */
@Composable
fun NavigationSuiteScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    startBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Row {
        startBar()
        Scaffold(
            modifier = modifier,
            topBar = topBar,
            bottomBar = bottomBar,
            content = content
        )
    }
}
