package com.nasdroid.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.boswelja.menuprovider.LocalMenuHost
import com.boswelja.menuprovider.MenuHost
import com.boswelja.menuprovider.material3.TopAppBarMenuItems
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    modalDrawerController: ModalDrawerController = LocalModalDrawerController.current,
    menuHost: MenuHost = LocalMenuHost.current,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val coroutineScope = rememberCoroutineScope()
    androidx.compose.material3.TopAppBar(
        title = title,
        navigationIcon = {
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
        actions = {
            TopAppBarMenuItems(menuHost = menuHost)
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior
    )
}
