package com.nasdroid.ui.navigation.bars

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nasdroid.core.menuprovider.LocalMenuHost
import com.nasdroid.core.menuprovider.MenuHost
import com.nasdroid.ui.navigation.MenuItem

enum class NavigationMode {
    None,
    Back,
    Drawer
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: @Composable () -> Unit,
    navigationMode: NavigationMode,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    menuHost: MenuHost = LocalMenuHost.current
) {
    androidx.compose.material3.TopAppBar(
        title = title,
        navigationIcon = {
            AnimatedContent(targetState = navigationMode, label = "Top App Bar Navigation Mode") {
                when (it) {
                    NavigationMode.None -> {}
                    NavigationMode.Back -> {
                        NavigateBackButton(onClick = onNavigationClick)
                    }
                    NavigationMode.Drawer -> {
                        IconButton(
                            onClick = onNavigationClick
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Navigation drawer")
                        }
                    }
                }
            }
        },
        actions = {
            menuHost.menuItems.forEach { menuItem ->
                MenuItem(menuItem)
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun NavigateBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Navigate back")
    }
}
