package com.nasdroid.navigation.navrail

import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nasdroid.navigation.NavigationItem
import com.nasdroid.navigation.NavigationRailItem
import com.nasdroid.navigation.drawer.LocalModalDrawerController
import com.nasdroid.navigation.drawer.ModalDrawerController
import com.nasdroid.navigation.topbar.NavDrawerIconButton

@Composable
internal fun StartNavRail(
    items: List<NavigationItem>,
    selectedItem: NavigationItem?,
    onItemClick: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    drawerController: ModalDrawerController = LocalModalDrawerController.current,
) {
    NavigationRail(
        header = {
            NavDrawerIconButton(drawerController)
        },
        modifier = modifier
    ) {
        items.forEach { item ->
            NavigationRailItem(
                item = item,
                selected = item == selectedItem,
                onClick = { onItemClick(item) }
            )
        }
    }
}
