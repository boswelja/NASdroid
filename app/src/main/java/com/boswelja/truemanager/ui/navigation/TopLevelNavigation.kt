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
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalNavigationDrawer(
    selectedDestination: TopLevelDestination?,
    destinations: List<TopLevelDestination>,
    navigateTo: (TopLevelDestination) -> Unit,
    navigationVisible: Boolean,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

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
                    }
                )
            },
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationRail(
    selectedDestination: TopLevelDestination?,
    destinations: List<TopLevelDestination>,
    navigateTo: (TopLevelDestination) -> Unit,
    navigationVisible: Boolean,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermanentNavigationDrawer(
    selectedDestination: TopLevelDestination?,
    destinations: List<TopLevelDestination>,
    navigateTo: (TopLevelDestination) -> Unit,
    navigationVisible: Boolean,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
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
        Text("Hello, world!",
            Modifier
                .padding(it)
                .fillMaxSize())
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
        Text("Hello, world!",
            Modifier
                .padding(it)
                .fillMaxSize())
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
        Text("Hello, world!",
            Modifier
                .padding(it)
                .fillMaxSize())
    }
}
