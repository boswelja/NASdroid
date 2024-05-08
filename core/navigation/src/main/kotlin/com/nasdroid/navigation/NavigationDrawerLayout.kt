package com.nasdroid.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NavigationDrawerLayout(
    drawerContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    navigationMode: NavigationMode = LocalNavigationMode.current,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    if (navigationMode == NavigationMode.PermanentNavigationDrawer) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(
                    content = drawerContent
                )
            },
            content = content,
            modifier = modifier
        )
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet(
                    drawerState = drawerState,
                    content = drawerContent
                )
            },
            drawerState = drawerState,
            content = content,
            modifier = modifier
        )
    }
}
