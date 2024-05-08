package com.nasdroid.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.boswelja.menuprovider.LocalMenuHost
import com.boswelja.menuprovider.MenuHost
import com.boswelja.menuprovider.material3.AnimatedTopAppBarMenuItems
import kotlinx.coroutines.launch

/**
 * An opinionated Scaffold that determines which mode of navigation is displayed based on
 * [navigationMode].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationSuiteScaffold(
    title: @Composable () -> Unit,
    navigationBarContent: @Composable RowScope.() -> Unit,
    navigationRailContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    navigationMode: NavigationMode = LocalNavigationMode.current,
    menuHost: MenuHost = LocalMenuHost.current,
    content: @Composable (PaddingValues) -> Unit
) {
    if (navigationMode == NavigationMode.NavigationRail) {
        val coroutineScope = rememberCoroutineScope()
        val modalDrawerController = LocalModalDrawerController.current
        Row(modifier) {
            NavigationRail(
                header = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                modalDrawerController.openDrawer()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "TODO")
                    }
                },
                content = navigationRailContent
            )
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = title,
                        actions = {
                            AnimatedTopAppBarMenuItems(menuHost = menuHost)
                        }
                    )
                },
                content = content
            )
        }
    } else {
        Scaffold(
            bottomBar = {
                if (navigationMode == NavigationMode.NavigationBar) {
                    NavigationBar(content = navigationBarContent)
                }
            },
            topBar = {
                if (navigationMode == NavigationMode.PermanentNavigationDrawer) {
                    // A permanent navigation drawer cannot be opened or closed, s owe don't show the
                    // option
                    TopAppBar(
                        title = title,
                        actions = {
                            AnimatedTopAppBarMenuItems(menuHost = menuHost)
                        }
                    )
                } else {
                    NavigableTopAppBar(title = title, menuHost = menuHost)
                }
            },
            modifier = modifier,
            content = content
        )
    }
}
