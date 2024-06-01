package com.nasdroid.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Displays either a [PermanentNavigationDrawer], or a [ModalNavigationDrawer], depending on the
 * state of [navigationMode].
 */
@Composable
fun NavigationDrawerLayout(
    drawerHeaderContent: @Composable () -> Unit,
    onNavigationItemClick: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    selectedItem: NavigationItem? = LocalSelectedNavigationItem.current,
    drawerItems: List<NavigationItem> = LocalNavigationDrawerItems.current,
    navigationMode: NavigationMode = LocalNavigationMode.current,
    content: @Composable () -> Unit
) {
    if (navigationMode.primaryNavigationMode == PrimaryNavigationMode.Permanent) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet {
                    drawerHeaderContent()
                    Spacer(Modifier.height(16.dp))
                    drawerItems.forEach { item ->
                        NavigationDrawerItem(
                            item = item,
                            selected = item == selectedItem,
                            onClick = { onNavigationItemClick(item) },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                }
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
        val coroutineScope = rememberCoroutineScope()
        CompositionLocalProvider(
            LocalModalDrawerController provides controller
        ) {
            ModalNavigationDrawer(
                drawerContent = {
                    ModalDrawerSheet(drawerState = drawerState) {
                        drawerHeaderContent()
                        Spacer(Modifier.height(16.dp))
                        drawerItems.forEach { item ->
                            NavigationDrawerItem(
                                item = item,
                                selected = item == selectedItem,
                                onClick = {
                                    onNavigationItemClick(item)
                                    coroutineScope.launch { drawerState.close() }
                                },
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                    }
                },
                drawerState = drawerState,
                content = content,
                modifier = modifier
            )
        }
    }
}

@Composable
internal fun NavigationDrawerItem(
    item: NavigationItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.NavigationDrawerItem(
        icon = { Icon(item.icon, contentDescription = null) },
        label = { Text(stringResource(item.labelRes)) },
        selected = selected,
        onClick = onClick,
        modifier = modifier
    )
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
