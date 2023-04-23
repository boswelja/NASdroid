package com.boswelja.truemanager.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopLevelNavigation(
    windowSizeClass: WindowSizeClass,
    selectedDestination: TopLevelDestination?,
    destinations: List<TopLevelDestination>,
    navigateTo: (TopLevelDestination) -> Unit,
    navigationVisible: Boolean,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            ModalNavigationDrawer(
                selectedDestination = selectedDestination,
                destinations = destinations,
                navigateTo = navigateTo,
                gesturesEnabled = navigationVisible,
                drawerState = drawerState,
                modifier = modifier
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { selectedDestination?.let { Text(stringResource(it.labelRes)) } },
                            navigationIcon = {
                                if (canNavigateBack) {
                                    IconButton(onClick = navigateBack) {
                                        Icon(Icons.Default.ArrowBack, contentDescription = "Navigation back")
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
                            }
                        )
                    },
                    content = content
                )
            }
        }
        WindowWidthSizeClass.Medium -> {
            Row(modifier) {
                AnimatedVisibility(
                    visible = navigationVisible,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally { -it/2 }
                ) {
                    NavigationRail {
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
                            }
                        )
                    },
                    content = content
                )
            }

        }
        WindowWidthSizeClass.Expanded -> {
            Row(modifier) {
                AnimatedVisibility(
                    visible = navigationVisible,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally { -it/2 }
                ) {
                    PermanentDrawerSheet {
                        Spacer(Modifier.height(12.dp))
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
                            }
                        )
                    },
                    content = content
                )
            }
        }
    }
}

@Composable
fun ModalNavigationDrawer(
    selectedDestination: TopLevelDestination?,
    destinations: List<TopLevelDestination>,
    navigateTo: (TopLevelDestination) -> Unit,
    drawerState: DrawerState,
    gesturesEnabled: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
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
        },
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        content = content,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = "spec:width=411dp,height=891dp")
@Composable
fun PhoneTopLevelNavigationPreview() {
    val destinations = remember {
        TopLevelDestination.values().toList()
    }
    var selectedDestination by remember {
        mutableStateOf(destinations.first())
    }
    TopLevelNavigation(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(width = 411.dp, height = 891.dp)),
        selectedDestination = selectedDestination,
        destinations = destinations,
        navigateTo = {
            selectedDestination = it
        },
        navigationVisible = true,
        canNavigateBack = false,
        navigateBack = {}
    ) {
        Text("Hello, world!",
            Modifier
                .padding(it)
                .fillMaxSize())
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun TabletTopLevelNavigationPreview() {
    val destinations = remember {
        TopLevelDestination.values().toList()
    }
    var selectedDestination by remember {
        mutableStateOf(destinations.first())
    }
    TopLevelNavigation(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(width = 1280.dp, height = 800.dp)),
        selectedDestination = selectedDestination,
        destinations = destinations,
        navigateTo = {
            selectedDestination = it
        },
        navigationVisible = true,
        canNavigateBack = false,
        navigateBack = {}
    ) {
        Text("Hello, world!",
            Modifier
                .padding(it)
                .fillMaxSize())
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = "spec:width=673dp,height=841dp")
@Composable
fun FoldableTopLevelNavigationPreview() {
    val destinations = remember {
        TopLevelDestination.values().toList()
    }
    var selectedDestination by remember {
        mutableStateOf(destinations.first())
    }
    TopLevelNavigation(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(width = 673.dp, height = 841.dp)),
        selectedDestination = selectedDestination,
        destinations = destinations,
        navigateTo = {
            selectedDestination = it
        },
        navigationVisible = true,
        canNavigateBack = false,
        navigateBack = {}
    ) {
        Text("Hello, world!",
            Modifier
                .padding(it)
                .fillMaxSize())
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = "spec:width=1920dp,height=1080dp,dpi=160")
@Composable
fun DesktopTopLevelNavigationPreview() {
    val destinations = remember {
        TopLevelDestination.values().toList()
    }
    var selectedDestination by remember {
        mutableStateOf(destinations.first())
    }
    TopLevelNavigation(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(width = 1920.dp, height = 1080.dp)),
        selectedDestination = selectedDestination,
        destinations = destinations,
        navigateTo = {
            selectedDestination = it
        },
        navigationVisible = true,
        canNavigateBack = false,
        navigateBack = {}
    ) {
        Text("Hello, world!",
            Modifier
                .padding(it)
                .fillMaxSize())
    }
}
