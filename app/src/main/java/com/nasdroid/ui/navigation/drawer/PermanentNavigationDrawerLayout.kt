package com.nasdroid.ui.navigation.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nasdroid.ui.navigation.TopLevelDestination2

/**
 * A Permanent Navigation Drawer with a top app bar to allow navigating back.
 *
 * @param selectedDestination The currently selected [TopLevelDestination2], or null if nothing is
 * selected.
 * @param navigateTo Called when the user navigates to a new destination.
 * @param modifier [Modifier].
 * @param navigationVisible Whether the user can see the navigation drawer.
 * @param content The screen content. The provided PaddingValues come from [Scaffold].
 */
@Composable
fun PermanentNavigationDrawerLayout(
    selectedDestination: TopLevelDestination2?,
    navigateTo: (TopLevelDestination2) -> Unit,
    modifier: Modifier = Modifier,
    navigationVisible: Boolean = true,
    content: @Composable () -> Unit,
) {
    val drawerScrollState = rememberScrollState()

    Row(modifier) {
        AnimatedVisibility(
            visible = navigationVisible,
            enter = slideInHorizontally(),
            exit = slideOutHorizontally { -it/2 }
        ) {
            TopLevelPermanentDrawerSheet(
                selectedDestination = selectedDestination,
                onClick = navigateTo,
                modifier = Modifier.fillMaxHeight().verticalScroll(drawerScrollState)
            )
        }
        content()
    }
}

@Composable
internal fun TopLevelPermanentDrawerSheet(
    selectedDestination: TopLevelDestination2?,
    onClick: (TopLevelDestination2) -> Unit,
    modifier: Modifier = Modifier,
) {
    PermanentDrawerSheet(
        modifier = modifier
    ) {
        Spacer(Modifier.height(12.dp))
        TopLevelDestination2.entries.forEach { destination ->
            TopLevelDrawerItem(
                destination = destination,
                onClick = { onClick(destination) },
                selected = destination == selectedDestination,
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}
