package com.nasdroid.ui.navigation.bar

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.boswelja.menuprovider.LocalMenuHost
import com.boswelja.menuprovider.MenuHost
import com.boswelja.menuprovider.material3.AnimatedTopAppBarMenuItems

/**
 * An opinionated Material3 TopAppBar optimized for NASdroid.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: @Composable () -> Unit,
    windowHeightSizeClass: WindowHeightSizeClass,
    navigationMode: TopAppBarNavigationMode,
    onNavigationClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    menuHost: MenuHost = LocalMenuHost.current
) {
    val navigationIcon = @Composable {
        AnimatedContent(targetState = navigationMode, label = "Top App Bar Navigation Mode") { mode ->
            when (mode) {
                TopAppBarNavigationMode.None -> {}
                TopAppBarNavigationMode.Back -> {
                    NavigateBackButton(onClick = onNavigationClick)
                }
                TopAppBarNavigationMode.Drawer -> {
                    IconButton(
                        onClick = onNavigationClick
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Navigation drawer")
                    }
                }
            }
        }
    }
    if (windowHeightSizeClass < WindowHeightSizeClass.Medium) {
        androidx.compose.material3.TopAppBar(
            title = title,
            navigationIcon = navigationIcon,
            actions = {
                AnimatedTopAppBarMenuItems(menuHost = menuHost)
            },
            modifier = modifier,
            scrollBehavior = scrollBehavior
        )
    } else {
        MediumTopAppBar(
            title = title,
            navigationIcon = navigationIcon,
            actions = {
                AnimatedTopAppBarMenuItems(
                    menuHost = menuHost
                )
            },
            modifier = modifier,
            scrollBehavior = scrollBehavior
        )
    }

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

/**
 * Describes all possible modes for the [TopAppBar] navigation icon.
 */
enum class TopAppBarNavigationMode {
    None,
    Back,
    Drawer
}
