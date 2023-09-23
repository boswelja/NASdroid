package com.nasdroid.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nasdroid.core.menuprovider.MenuItem
import com.nasdroid.core.menuprovider.ProvideMenuHost
import com.nasdroid.core.menuprovider.ProvideMenuItems
import com.nasdroid.core.menuprovider.rememberMenuHost
import com.nasdroid.ui.navigation.bar.BottomNavigationBar
import com.nasdroid.ui.navigation.bar.NavigationMode
import com.nasdroid.ui.navigation.bar.StartNavigationRail
import com.nasdroid.ui.navigation.drawer.ModalNavigationDrawerLayout
import com.nasdroid.ui.navigation.drawer.PermanentNavigationDrawerLayout
import kotlinx.coroutines.launch

private val BottomNavDestinations = listOf(
    TopLevelDestination.Dashboard,
    TopLevelDestination.Storage,
    TopLevelDestination.Shares,
    TopLevelDestination.Apps,
    TopLevelDestination.Network
)

private val StartNavRailDestinations = listOf(
    TopLevelDestination.Dashboard,
    TopLevelDestination.Storage,
    TopLevelDestination.Shares,
    TopLevelDestination.Apps,
    TopLevelDestination.Network,
    TopLevelDestination.Reporting
)

/**
 * Orchestrates all top-level navigation components. The navigation components displayed depend on
 * [windowSizeClass].
 */
@Composable
fun TopLevelNavigation(
    windowSizeClass: WindowSizeClass,
    selectedDestination: TopLevelDestination?,
    navigateTo: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    navigationVisible: Boolean = true,
    canNavigateBack: Boolean = false,
    navigateBack: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            ModalNavigationDrawer(
                selectedDestination = selectedDestination,
                destinations = BottomNavDestinations,
                navigateTo = navigateTo,
                navigationVisible = navigationVisible,
                canNavigateBack = canNavigateBack,
                navigateBack = navigateBack,
                content = content,
                modifier = modifier,
            )
        }
        WindowWidthSizeClass.Medium -> {
            NavigationRail(
                selectedDestination = selectedDestination,
                destinations = StartNavRailDestinations,
                navigateTo = navigateTo,
                navigationVisible = navigationVisible,
                canNavigateBack = canNavigateBack,
                navigateBack = navigateBack,
                content = content,
                modifier = modifier,
            )
        }
        WindowWidthSizeClass.Expanded -> {
            PermanentNavigationDrawer(
                selectedDestination = selectedDestination,
                navigateTo = navigateTo,
                navigationVisible = navigationVisible,
                canNavigateBack = canNavigateBack,
                navigateBack = navigateBack,
                content = content,
                modifier = modifier,
            )
        }
    }
}

/**
 * A Modal navigation drawer with a top app bar to control its state and display the current
 * destination.
 *
 * @param selectedDestination The currently selected [TopLevelDestination], or null if nothing is
 * selected.
 * @param destinations A list of [TopLevelDestination]s that should be displayed.
 * @param navigateTo Called when the user navigates to a new destination.
 * @param modifier [Modifier].
 * @param navigationVisible Whether the user can open the modal navigation drawer.
 * @param canNavigateBack Whether the back button should be displayed. When this is true, the user
 * cannot access the navigation drawer.
 * @param navigateBack Called when the user navigates back.
 * @param content The screen content. The provided PaddingValues come from [Scaffold].
 */
@Composable
fun ModalNavigationDrawer(
    selectedDestination: TopLevelDestination?,
    destinations: List<TopLevelDestination>,
    navigateTo: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    navigationVisible: Boolean = true,
    canNavigateBack: Boolean = false,
    navigateBack: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val menuHost = rememberMenuHost()

    ProvideMenuHost(menuHost = menuHost) {
        ModalNavigationDrawerLayout(
            drawerState = drawerState,
            selectedDestination = selectedDestination,
            navigateTo = navigateTo,
            gesturesEnabled = navigationVisible,
            modifier = modifier,
        ) {
            Scaffold(
                topBar = {
                    if (canNavigateBack || navigationVisible) {
                        com.nasdroid.ui.navigation.bar.TopAppBar(
                            title = { selectedDestination?.let { Text(stringResource(it.labelRes)) } },
                            navigationMode = if (canNavigateBack) NavigationMode.Back else NavigationMode.Drawer,
                            onNavigationClick = {
                                if (canNavigateBack) {
                                    navigateBack()
                                } else {
                                    coroutineScope.launch { drawerState.open() }
                                }
                            }
                        )
                    }
                },
                bottomBar = {
                    if (navigationVisible) {
                        BottomNavigationBar(
                            destinations = destinations,
                            selectedDestination = selectedDestination,
                            onDestinationClick = navigateTo
                        )
                    }
                },
                content = content
            )
        }
    }
}

/**
 * A Navigation Rail with a top app bar to allow navigating back.
 *
 * @param selectedDestination The currently selected [TopLevelDestination], or null if nothing is
 * selected.
 * @param destinations A list of [TopLevelDestination]s that should be displayed.
 * @param navigateTo Called when the user navigates to a new destination.
 * @param modifier [Modifier].
 * @param navigationVisible Whether the user can see the navigation rail.
 * @param canNavigateBack Whether the back button should be displayed.
 * @param navigateBack Called when the user navigates back.
 * @param content The screen content. The provided PaddingValues come from [Scaffold].
 */
@Composable
fun NavigationRail(
    selectedDestination: TopLevelDestination?,
    destinations: List<TopLevelDestination>,
    navigateTo: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    navigationVisible: Boolean = true,
    canNavigateBack: Boolean = false,
    navigateBack: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val menuHost = rememberMenuHost()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ProvideMenuHost(menuHost = menuHost) {
        ModalNavigationDrawerLayout(
            drawerState = drawerState,
            selectedDestination = selectedDestination,
            gesturesEnabled = navigationVisible,
            navigateTo = navigateTo
        ) {
            Row(modifier) {
                AnimatedVisibility(
                    visible = navigationVisible,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally { -it/2 }
                ) {
                    StartNavigationRail(
                        destinations = destinations,
                        selectedDestination = selectedDestination,
                        onDestinationClick = navigateTo,
                        onNavigationClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }
                    )
                }
                Scaffold(
                    topBar = {
                        if (navigationVisible || canNavigateBack) {
                            com.nasdroid.ui.navigation.bar.TopAppBar(
                                title = { selectedDestination?.let { Text(stringResource(it.labelRes)) } },
                                navigationMode = if (canNavigateBack) NavigationMode.Back else NavigationMode.None,
                                onNavigationClick = navigateBack
                            )
                        }
                    },
                    content = content
                )
            }
        }
    }
}

/**
 * A Permanent Navigation Drawer with a top app bar to allow navigating back.
 *
 * @param selectedDestination The currently selected [TopLevelDestination], or null if nothing is
 * selected.
 * @param navigateTo Called when the user navigates to a new destination.
 * @param modifier [Modifier].
 * @param navigationVisible Whether the user can see the navigation drawer.
 * @param canNavigateBack Whether the back button should be displayed.
 * @param navigateBack Called when the user navigates back.
 * @param content The screen content. The provided PaddingValues come from [Scaffold].
 */
@Composable
fun PermanentNavigationDrawer(
    selectedDestination: TopLevelDestination?,
    navigateTo: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    navigationVisible: Boolean = true,
    canNavigateBack: Boolean = false,
    navigateBack: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val menuHost = rememberMenuHost()

    ProvideMenuHost(menuHost = menuHost) {
        PermanentNavigationDrawerLayout(
            selectedDestination = selectedDestination,
            navigateTo = navigateTo,
            navigationVisible = navigationVisible,
            modifier = modifier,
        ) {
            Scaffold(
                topBar = {
                    if (navigationVisible || canNavigateBack) {
                        com.nasdroid.ui.navigation.bar.TopAppBar(
                            title = { selectedDestination?.let { Text(stringResource(it.labelRes)) } },
                            navigationMode = if (canNavigateBack) NavigationMode.Back else NavigationMode.None,
                            onNavigationClick = navigateBack
                        )
                    }
                },
                content = content
            )
        }
    }
}

@Composable
internal fun MenuItem(
    menuItem: MenuItem,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = menuItem.onClick, modifier = modifier) {
        Icon(
            imageVector = menuItem.imageVector,
            contentDescription = menuItem.label
        )
    }
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
fun ModalNavigationDrawerPreview() {
    val destinations = BottomNavDestinations
    var selectedDestination by remember {
        mutableStateOf(destinations.first())
    }
    ModalNavigationDrawer(
        selectedDestination = selectedDestination,
        destinations = destinations,
        navigateTo = {
            selectedDestination = it
        },
        navigationVisible = true,
        canNavigateBack = false,
        navigateBack = {}
    ) {
        ProvideMenuItems(
            MenuItem(
                label = "Edit",
                imageVector = Icons.Default.Edit,
                onClick = {},
                isImportant = true
            )
        )
        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
    }
}

@Preview(device = "spec:width=673dp,height=841dp")
@Composable
fun NavigationRailPreview() {
    val destinations = StartNavRailDestinations
    var selectedDestination by remember {
        mutableStateOf(destinations.first())
    }
    NavigationRail(
        selectedDestination = selectedDestination,
        destinations = destinations,
        navigateTo = {
            selectedDestination = it
        },
        navigationVisible = true,
        canNavigateBack = true,
        navigateBack = {}
    ) {
        ProvideMenuItems(
            MenuItem(
                label = "More",
                imageVector = Icons.Default.MoreVert,
                onClick = {},
                isImportant = true
            )
        )
        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun PermanentNavigationDrawerPreview() {
    val destinations = TopLevelDestination.entries
    var selectedDestination by remember {
        mutableStateOf(destinations.first())
    }
    PermanentNavigationDrawer(
        selectedDestination = selectedDestination,
        navigateTo = {
            selectedDestination = it
        },
        navigationVisible = true,
        canNavigateBack = true,
        navigateBack = {}
    ) {
        ProvideMenuItems(
            MenuItem(
                label = "Stop editing",
                imageVector = Icons.Default.EditOff,
                onClick = {},
                isImportant = true
            )
        )
        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
    }
}
