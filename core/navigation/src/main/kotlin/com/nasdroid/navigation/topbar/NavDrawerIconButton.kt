package com.nasdroid.navigation.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.nasdroid.navigation.ModalDrawerController
import kotlinx.coroutines.launch

@Composable
internal fun NavDrawerIconButton(
    drawerController: ModalDrawerController,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    IconButton(
        onClick = {
            coroutineScope.launch {
                drawerController.openDrawer()
            }
        },
        modifier = modifier
    ) {
        Icon(Icons.Default.Menu, contentDescription = "TODO")
    }
}