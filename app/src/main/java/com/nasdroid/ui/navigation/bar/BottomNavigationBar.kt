package com.nasdroid.ui.navigation.bar

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.ui.navigation.TopLevelDestination

/**
 * An opinionated Material3 [NavigationBar] that displays the provided list of
 * [TopLevelDestination]s.
 */
@Composable
fun BottomNavigationBar(
    destinations: List<TopLevelDestination>,
    selectedDestination: TopLevelDestination?,
    onDestinationClick: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
    ) {
        Spacer(Modifier.width(MaterialThemeExt.paddings.small))
        destinations.forEach { destination ->
            NavigationBarItem(
                selected = destination == selectedDestination,
                onClick = { onDestinationClick(destination) },
                icon = { Icon(imageVector = destination.icon, contentDescription = null) },
                label = {
                    Text(
                        text = stringResource(destination.labelRes),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                alwaysShowLabel = true
            )
        }
        Spacer(Modifier.width(MaterialThemeExt.paddings.small))
    }
}
