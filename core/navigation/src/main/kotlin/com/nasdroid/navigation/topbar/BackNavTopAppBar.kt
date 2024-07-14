package com.nasdroid.navigation.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.boswelja.menuprovider.LocalMenuHost
import com.boswelja.menuprovider.MenuHost
import com.boswelja.menuprovider.material3.AnimatedTopAppBarMenuItems

/**
 * An opinionated TopAppBar that allows the user to navigate back to the previous screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackNavTopAppBar(
    title: @Composable () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    menuHost: MenuHost = LocalMenuHost.current,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = title,
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "TODO")
            }
        },
        actions = { AnimatedTopAppBarMenuItems(menuHost = menuHost) },
        modifier = modifier,
        scrollBehavior = scrollBehavior
    )
}
