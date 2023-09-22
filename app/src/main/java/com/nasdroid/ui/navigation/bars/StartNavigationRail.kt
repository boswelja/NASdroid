package com.nasdroid.ui.navigation.bars

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nasdroid.ui.navigation.TopLevelDestination

@Composable
fun StartNavigationRail(
    destinations: List<TopLevelDestination>,
    selectedDestination: TopLevelDestination?,
    onDestinationClick: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(modifier = modifier) {
        destinations.forEach { destination ->
            NavigationRailItem(
                selected = destination == selectedDestination,
                onClick = { onDestinationClick(destination) },
                icon = { Icon(destination.icon, contentDescription = null) },
                label = { Text(stringResource(destination.labelRes)) }
            )
        }
    }
}