package com.boswelja.truemanager.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.core.menuprovider.LocalMenuHost
import com.boswelja.truemanager.core.menuprovider.MenuItem
import com.boswelja.truemanager.core.menuprovider.ProvideMenuHost
import com.boswelja.truemanager.core.menuprovider.ProvideMenuItems
import com.boswelja.truemanager.core.menuprovider.rememberMenuHost
import kotlinx.coroutines.launch

/**
 * Orchestrates all top-level navigation components. The navigation components displayed depend on
 * [windowSizeClass].
 */
@Composable
fun TopLevelNavigation(
    windowSizeClass: WindowSizeClass,
    selectedDestination: TopLevelDestination?,
    destinations: List<TopLevelDestination>,
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
                destinations = destinations,
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
                destinations = destinations,
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
                destinations = destinations,
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
@OptIn(ExperimentalMaterial3Api::class)
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
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(Modifier.height(12.dp))
                    destinations.forEach { destination ->
                        NavigationDrawerItem(
                            icon = { Icon(destination.icon, contentDescription = null) },
                            label = { Text(stringResource(destination.labelRes)) },
                            selected = destination == selectedDestination,
                            onClick = {
                                navigateTo(destination)
                                coroutineScope.launch {
                                    drawerState.close()
                                }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            },
            drawerState = drawerState,
            gesturesEnabled = navigationVisible && !canNavigateBack,
            modifier = modifier
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { selectedDestination?.let { Text(stringResource(it.labelRes)) } },
                        navigationIcon = {
                            if (canNavigateBack) {
                                IconButton(onClick = navigateBack) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Navigate back")
                                }
                            } else if (navigationVisible) {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            drawerState.open()
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.Menu, contentDescription = "Navigation drawer")
                                }
                            }
                        },
                        actions = {
                            LocalMenuHost.current.menuItems.forEach { menuItem ->
                                IconButton(onClick = menuItem.onClick) {
                                    Icon(
                                        imageVector = menuItem.imageVector,
                                        contentDescription = menuItem.label
                                    )
                                }
                            }
                        }
                    )
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
@OptIn(ExperimentalMaterial3Api::class)
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

    ProvideMenuHost(menuHost = menuHost) {
        Row(modifier) {
            AnimatedVisibility(
                visible = navigationVisible,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally { -it/2 }
            ) {
                NavigationRail {
                    Spacer(Modifier.height(12.dp + 64.dp))
                    destinations.forEach { destination ->
                        val label = stringResource(destination.labelRes)
                        NavigationRailItem(
                            selected = destination == selectedDestination,
                            onClick = { navigateTo(destination) },
                            icon = { Icon(destination.icon, contentDescription = label) },
                            label = { Text(label) }
                        )
                    }
                }
            }
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { },
                        navigationIcon = {
                            if (canNavigateBack) {
                                IconButton(onClick = navigateBack) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Navigation back")
                                }
                            }
                        },
                        actions = {
                            LocalMenuHost.current.menuItems.forEach { menuItem ->
                                IconButton(onClick = menuItem.onClick) {
                                    Icon(
                                        imageVector = menuItem.imageVector,
                                        contentDescription = menuItem.label
                                    )
                                }
                            }
                        }
                    )
                },
                content = content
            )
        }
    }
}

/**
 * A Permanent Navigation Drawer with a top app bar to allow navigating back.
 *
 * @param selectedDestination The currently selected [TopLevelDestination], or null if nothing is
 * selected.
 * @param destinations A list of [TopLevelDestination]s that should be displayed.
 * @param navigateTo Called when the user navigates to a new destination.
 * @param modifier [Modifier].
 * @param navigationVisible Whether the user can see the navigation drawer.
 * @param canNavigateBack Whether the back button should be displayed.
 * @param navigateBack Called when the user navigates back.
 * @param content The screen content. The provided PaddingValues come from [Scaffold].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermanentNavigationDrawer(
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

    ProvideMenuHost(menuHost = menuHost) {
        Row(modifier) {
            AnimatedVisibility(
                visible = navigationVisible,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally { -it/2 }
            ) {
                PermanentDrawerSheet {
                    Spacer(Modifier.height(12.dp + 64.dp))
                    destinations.forEach { destination ->
                        NavigationDrawerItem(
                            icon = { Icon(destination.icon, contentDescription = null) },
                            label = { Text(stringResource(destination.labelRes)) },
                            selected = destination == selectedDestination,
                            onClick = { navigateTo(destination) },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { },
                        navigationIcon = {
                            if (canNavigateBack) {
                                IconButton(onClick = navigateBack) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Navigation back")
                                }
                            }
                        },
                        actions = {
                            LocalMenuHost.current.menuItems.forEach { menuItem ->
                                IconButton(onClick = menuItem.onClick) {
                                    Icon(
                                        imageVector = menuItem.imageVector,
                                        contentDescription = menuItem.label
                                    )
                                }
                            }
                        }
                    )
                },
                content = content
            )
        }
    }
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
fun ModalNavigationDrawerPreview() {
    val destinations = remember {
        TopLevelDestination.values().toList()
    }
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
                label = "Menu item",
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
    val destinations = remember {
        TopLevelDestination.values().toList()
    }
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
                label = "Menu item",
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

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun PermanentNavigationDrawerPreview() {
    val destinations = remember {
        TopLevelDestination.values().toList()
    }
    var selectedDestination by remember {
        mutableStateOf(destinations.first())
    }
    PermanentNavigationDrawer(
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
                label = "Menu item",
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
