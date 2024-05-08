package com.nasdroid.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationSuiteScaffold(
    title: @Composable () -> Unit,
    navigationBarContent: @Composable RowScope.() -> Unit,
    navigationRailContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    navigationMode: NavigationMode = LocalNavigationMode.current,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            if (navigationMode == NavigationMode.NavigationBar) {
                NavigationBar(content = navigationBarContent)
            }
        },
        topBar = {
            if (navigationMode == NavigationMode.PermanentNavigationDrawer) {
                TopAppBar(title = title)
            } else {
                NavigationTopAppBar(title = title)
            }
        },
        modifier = modifier
    ) { insets ->
        if (navigationMode == NavigationMode.NavigationRail) {
            Row {
                NavigationRail(content = navigationRailContent)
                content(insets)
            }
        } else {
            content(insets)
        }
    }
}
