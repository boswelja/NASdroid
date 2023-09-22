package com.nasdroid.ui.navigation.drawer

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nasdroid.ui.navigation.TopLevelDestination

@Composable
fun TopLevelDrawerItem(
    destination: TopLevelDestination,
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    NavigationDrawerItem(
        icon = { Icon(destination.icon, contentDescription = null) },
        label = { Text(stringResource(destination.labelRes)) },
        selected = selected,
        onClick = onClick,
        modifier = modifier
    )
}
