package com.nasdroid.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.boswelja.menuprovider.LocalMenuHost
import com.boswelja.menuprovider.MenuHost
import com.boswelja.menuprovider.MenuItem
import com.boswelja.menuprovider.ProvideMenuItems
import com.boswelja.menuprovider.ProvideSingleTopMenuHost
import com.boswelja.menuprovider.material3.AnimatedTopAppBarMenuItems
import com.nasdroid.design.MaterialThemeExt
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
        val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        ProvideSingleTopMenuHost {
            NavigationSuiteScaffold(
                topBar = {
                    if (canNavigateBack || navigationVisible) {
                        TopNavigation(
                            selectedDestination = selectedDestination,
                            navigationMode = navigationMode,
                            canNavigateBack = canNavigateBack,
                            onOpenModalNavigation = { coroutineScope.launch { modalDrawerState.open() } },
                            onNavigateBack = navigateBack,
                            scrollBehavior = topAppBarScrollBehavior,
                        )
                    }
                },
                sideBar = {
                    if (navigationVisible) {
                        SideNavigation(
                            selectedDestination = selectedDestination,
                            onDestinationClick = navigateTo,
                            onOpenModalNavigation = { coroutineScope.launch { modalDrawerState.open() } },
                            navigationMode = navigationMode
                        )
                    }
                },
                bottomBar = {
                    if (navigationVisible) {
                        BottomNavigation(
                            selectedDestination = selectedDestination,
                            onDestinationClick = navigateTo,
                            navigationMode = navigationMode
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

@Suppress("LongParameterList")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopNavigation(
    selectedDestination: TopLevelDestination?,
    navigationMode: NavigationMode,
    canNavigateBack: Boolean,
    onOpenModalNavigation: () -> Unit,
    onNavigateBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    menuHost: MenuHost = LocalMenuHost.current,
) {
    TopAppBar(
        title = { selectedDestination?.let { Text(stringResource(it.labelRes)) } },
        navigationIcon = {
            AnimatedContent(targetState = canNavigateBack, label = "Top App Bar Navigation Mode") { showBack ->
                if (showBack) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Navigate back")
                    }
                } else if (!navigationMode.secondaryNavigationMode.providesPrimaryNavigationLauncher) {
                    IconButton(onClick = onOpenModalNavigation) {
                        Icon(Icons.Default.Menu, contentDescription = "Navigation drawer")
                    }
                }
            }
        },
        actions = {
            AnimatedTopAppBarMenuItems(menuHost = menuHost)
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior
    )
}

@Composable
internal fun SideNavigation(
    selectedDestination: TopLevelDestination?,
    onDestinationClick: (TopLevelDestination) -> Unit,
    onOpenModalNavigation: () -> Unit,
    navigationMode: NavigationMode,
    modifier: Modifier = Modifier,
    destinations: List<TopLevelDestination> = StartNavRailDestinations
) {
   if (navigationMode.secondaryNavigationMode == SecondaryNavigationMode.StartNavRail) {
       NavigationRail(
           modifier = modifier,
           header = {
               IconButton(onClick = onOpenModalNavigation) {
                   Icon(Icons.Default.Menu, contentDescription = null)
               }
           }
       ) {
           Spacer(Modifier.weight(1f))
           destinations.forEach { destination ->
               NavigationRailItem(
                   selected = destination == selectedDestination,
                   onClick = { onDestinationClick(destination) },
                   icon = { Icon(destination.icon, contentDescription = null) },
                   label = { Text(stringResource(destination.labelRes)) }
               )
           }
           Spacer(Modifier.weight(1f))
       }
   }
}

@Composable
internal fun BottomNavigation(
    selectedDestination: TopLevelDestination?,
    onDestinationClick: (TopLevelDestination) -> Unit,
    navigationMode: NavigationMode,
    modifier: Modifier = Modifier,
    destinations: List<TopLevelDestination> = BottomNavDestinations
) {
    if (navigationMode.secondaryNavigationMode == SecondaryNavigationMode.BottomNavBar) {
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
