package com.nasdroid

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SettingsPower
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nasdroid.auth.ui.selector.DrawerServerSelector
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.navigation.LocalNavigationMode
import com.nasdroid.navigation.NavigationDrawerLayout
import com.nasdroid.navigation.NavigationItem
import com.nasdroid.navigation.NavigationModeDefaults.calculateFromAdaptiveInfo
import com.nasdroid.navigation.ProvideNavigationItems
import com.nasdroid.power.ui.PowerOptionsDialog
import com.nasdroid.ui.navigation.TopLevelDestination


/**
 * The main content of the app. This Composable displays navigation-related elements, and contains a
 * NavHost for features to display their own screens.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreen(
    onLogOut: () -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val destinations = remember {
        TopLevelDestination.entries
    }

    val navController = rememberNavController()
    val currentBackstackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination by remember {
        derivedStateOf {
            val currentRoute = currentBackstackEntry?.destination?.parent?.route
            destinations.firstOrNull { it.getRoute() == currentRoute }
        }
    }

    var isPowerOptionsVisible by rememberSaveable { mutableStateOf(false) }

    ProvideNavigationItems(
        selectedNavigationItem = selectedDestination?.let { NavigationItem(it.icon, it.labelRes, it.getRoute()) },
        bottomNavigationItems = emptyList(),
        navigationRailItems = emptyList(),
        navigationDrawerItems = emptyList()
    ) {
        CompositionLocalProvider(
            LocalNavigationMode provides calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
        ) {
            NavigationDrawerLayout(
                drawerHeaderContent = {
                    DrawerServerSelector(
                        onLogout = onLogOut,
                        modifier = Modifier.padding(horizontal = MaterialThemeExt.paddings.large),
                        additionalControls = {
                            OutlinedIconButton(onClick = { isPowerOptionsVisible = true }) {
                                Icon(Icons.Default.SettingsPower, "Power")
                            }
                        }
                    )
                },
                onNavigationItemClick = { navController.navigate(it.route) },
                modifier = modifier
            ) {
                MainNavHost(
                    navController = navController,
                    destinations = destinations,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    windowSizeClass = windowSizeClass,
                )
            }
        }
    }
    if (isPowerOptionsVisible) {
        PowerOptionsDialog(
            onDismiss = { isPowerOptionsVisible = false }
        )
    }
}
