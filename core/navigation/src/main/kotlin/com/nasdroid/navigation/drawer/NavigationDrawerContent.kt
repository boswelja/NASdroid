package com.nasdroid.navigation.drawer

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nasdroid.navigation.NavigationItem

@Composable
internal fun ColumnScope.NavigationDrawerContent(
    items: List<NavigationItem>,
    selectedItem: NavigationItem?,
    onItemClick: (NavigationItem) -> Unit,
    headerContent: @Composable () -> Unit,
) {
    headerContent()
    Spacer(Modifier.height(16.dp))
    items.forEach { item ->
        NavigationDrawerItem(
            item = item,
            selected = item == selectedItem,
            onClick = {
                onItemClick(item)
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )
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
