package com.nasdroid.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.boswelja.menuprovider.MenuItem
import com.boswelja.menuprovider.ProvideMenuItems
import com.boswelja.menuprovider.ProvideSingleTopMenuHost
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.ui.navigation.bar.BottomNavigationBar
import com.nasdroid.ui.navigation.bar.StartNavigationRail
import com.nasdroid.ui.navigation.bar.TopAppBar
import com.nasdroid.ui.navigation.bar.TopAppBarNavigationMode
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
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
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
    val navigationMode = NavigationMode.rememberFromWindowSize(windowSizeClass)
    val modalDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val navigationScaffold = @Composable {
        val coroutineScope = rememberCoroutineScope()
        val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        ProvideSingleTopMenuHost {
            NavigationSuiteScaffold(
                topBar = {
                    if (canNavigateBack || navigationVisible) {
                        TopAppBar(
                            title = { selectedDestination?.let { Text(stringResource(it.labelRes)) } },
                            navigationMode = if (canNavigateBack) {
                                TopAppBarNavigationMode.Back
                            } else if (
                                navigationMode.primaryNavigationMode == PrimaryNavigationMode.Modal &&
                                navigationMode.secondaryNavigationMode != SecondaryNavigationMode.StartNavRail
                            ) {
                                TopAppBarNavigationMode.Drawer
                            } else {
                                TopAppBarNavigationMode.None
                            },
                            onNavigationClick = {
                                if (canNavigateBack) {
                                    navigateBack()
                                } else {
                                    coroutineScope.launch { modalDrawerState.open() }
                                }
                            },
                            windowHeightSizeClass = windowSizeClass.heightSizeClass,
                            scrollBehavior = topAppBarScrollBehavior
                        )
                    }
                },
                startBar = {
                    if (
                        navigationVisible &&
                        navigationMode.secondaryNavigationMode == SecondaryNavigationMode.StartNavRail
                    ) {
                        StartNavigationRail(
                            destinations = StartNavRailDestinations,
                            selectedDestination = selectedDestination,
                            onDestinationClick = navigateTo,
                            onNavigationClick = {
                                coroutineScope.launch { modalDrawerState.open() }
                            }
                        )
                    }
                },
                bottomBar = {
                    if (
                        navigationVisible &&
                        navigationMode.secondaryNavigationMode == SecondaryNavigationMode.BottomNavBar
                    ) {
                        BottomNavigationBar(
                            destinations = BottomNavDestinations,
                            selectedDestination = selectedDestination,
                            onDestinationClick = navigateTo
                        )
                    }
                },
                content = content,
                modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            )
        }
    }
    when (navigationMode.primaryNavigationMode) {
        PrimaryNavigationMode.Modal -> {
            ModalNavigationDrawerLayout(
                drawerState = modalDrawerState,
                selectedDestination = selectedDestination,
                navigateTo = navigateTo,
                content = navigationScaffold,
                modifier = modifier
            )
        }
        PrimaryNavigationMode.Permanent -> {
            PermanentNavigationDrawerLayout(
                selectedDestination = selectedDestination,
                navigateTo = navigateTo,
                content = navigationScaffold,
                modifier = modifier
            )
        }
    }
}

/**
 * All possible states for the apps primary mode of navigation.
 */
enum class PrimaryNavigationMode {
    Modal,
    Permanent
}

/**
 * All possible states for the apps secondary mode of navigation.
 */
enum class SecondaryNavigationMode {
    None,
    BottomNavBar,
    StartNavRail
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = "spec:width=411dp,height=891dp")
@Composable
fun PortraitPhonePreviewPreview() {
    val destinations = BottomNavDestinations
    var selectedDestination by remember {
        mutableStateOf(destinations.first())
    }
    val windowSizeClass = WindowSizeClass.calculateFromSize(size = DpSize(411.dp, 891.dp))
    TopLevelNavigation(
        windowSizeClass = windowSizeClass,
        selectedDestination = selectedDestination,
        navigateTo = { selectedDestination = it }
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
                .background(MaterialThemeExt.colorScheme.primaryContainer)
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = "spec:width=891dp,height=411dp")
@Composable
fun LandscapePhonePreviewPreview() {
    var selectedDestination by remember {
        mutableStateOf(TopLevelDestination.Dashboard)
    }
    val windowSizeClass = WindowSizeClass.calculateFromSize(size = DpSize(891.dp, 411.dp))
    TopLevelNavigation(
        windowSizeClass = windowSizeClass,
        selectedDestination = selectedDestination,
        navigateTo = { selectedDestination = it }
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
                .background(MaterialThemeExt.colorScheme.primaryContainer)
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = "spec:width=673dp,height=841dp")
@Composable
fun PortraitUnfoldedFoldablePreview() {
    var selectedDestination by remember {
        mutableStateOf(TopLevelDestination.Dashboard)
    }
    val windowSizeClass = WindowSizeClass.calculateFromSize(size = DpSize(673.dp, 841.dp))
    TopLevelNavigation(
        windowSizeClass = windowSizeClass,
        selectedDestination = selectedDestination,
        navigateTo = { selectedDestination = it }
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
                .background(MaterialThemeExt.colorScheme.primaryContainer)
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = "spec:width=841dp,height=673dp")
@Composable
fun LandscapeUnfoldedFoldablePreview() {
    var selectedDestination by remember {
        mutableStateOf(TopLevelDestination.Dashboard)
    }
    val windowSizeClass = WindowSizeClass.calculateFromSize(size = DpSize(841.dp, 673.dp))
    TopLevelNavigation(
        windowSizeClass = windowSizeClass,
        selectedDestination = selectedDestination,
        navigateTo = { selectedDestination = it }
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
                .background(MaterialThemeExt.colorScheme.primaryContainer)
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = "spec:width=1280dp,height=800dp")
@Composable
fun LandscapeTabletPreview() {
    var selectedDestination by remember {
        mutableStateOf(TopLevelDestination.Dashboard)
    }
    val windowSizeClass = WindowSizeClass.calculateFromSize(size = DpSize(1280.dp, 800.dp))
    TopLevelNavigation(
        windowSizeClass = windowSizeClass,
        selectedDestination = selectedDestination,
        navigateTo = { selectedDestination = it }
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
                .background(MaterialThemeExt.colorScheme.primaryContainer)
        )
    }
}
