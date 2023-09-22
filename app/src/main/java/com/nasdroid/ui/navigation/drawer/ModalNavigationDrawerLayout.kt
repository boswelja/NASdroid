package com.nasdroid.ui.navigation.drawer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nasdroid.ui.navigation.TopLevelDestination
import kotlinx.coroutines.launch

/**
 * A Modal navigation drawer with a top app bar to control its state and display the current
 * destination. This displays all [TopLevelDestination]s.
 *
 * @param drawerState The [DrawerState] for the modal drawer.
 * @param selectedDestination The currently selected [TopLevelDestination], or null if nothing is
 * selected.
 * @param navigateTo Called when the user navigates to a new destination.
 * @param modifier [Modifier].
 * @param gesturesEnabled Whether the modal drawer can be opened by gestures.
 * @param content The screen content. The provided PaddingValues come from [Scaffold].
 */
@Composable
fun ModalNavigationDrawerLayout(
    drawerState: DrawerState,
    selectedDestination: TopLevelDestination?,
    navigateTo: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    gesturesEnabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerScrollState = rememberScrollState()

    ModalNavigationDrawer(
        drawerContent = {
            TopLevelModalDrawerSheet(
                selectedDestination = selectedDestination,
                onClick = {
                    navigateTo(it)
                    coroutineScope.launch { drawerState.close() }
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(drawerScrollState)
            )
        },
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        modifier = modifier,
        content = content
    )
}

@Composable
internal fun TopLevelModalDrawerSheet(
    selectedDestination: TopLevelDestination?,
    onClick: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalDrawerSheet(
        modifier = modifier
    ) {
        Spacer(Modifier.height(12.dp))
        TopLevelDestination.entries.forEach { destination ->
            TopLevelDrawerItem(
                destination = destination,
                onClick = {
                    onClick(destination)
                },
                selected = destination == selectedDestination,
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
            )
        }
    }
}
