package com.nasdroid

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lan
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsPower
import androidx.compose.material.icons.filled.Storage
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

internal val NavigationDrawerItems = listOf(
    NavigationItem(
        icon = Icons.Default.Dashboard,
        labelRes = R.string.feature_dashboard,
        route = "dashboard"
    ),
    NavigationItem(
        icon = Icons.Default.Storage,
        labelRes = R.string.feature_storage,
        route = "storage"
    ),
    NavigationItem(
        icon = Icons.Default.Dataset,
        labelRes = R.string.feature_datasets,
        route = "datasets"
    ),
    NavigationItem(
        icon = Icons.Default.FolderShared,
        labelRes = R.string.feature_shares,
        route = "shares"
    ),
    NavigationItem(
        icon = Icons.Default.Security,
        labelRes = R.string.feature_data_protection,
        route = "data_protection"
    ),
    NavigationItem(
        icon = Icons.Default.Lan,
        labelRes = R.string.feature_network,
        route = "network"
    ),
    NavigationItem(
        icon = Icons.Default.Key,
        labelRes = R.string.feature_credentials,
        route = "credentials"
    ),
    NavigationItem(
        icon = Icons.Default.Computer,
        labelRes = R.string.feature_virtualization,
        route = "virtualization"
    ),
    NavigationItem(
        icon = Icons.Default.Apps,
        labelRes = R.string.feature_apps,
        route = "apps"
    ),
    NavigationItem(
        icon = Icons.Default.Analytics,
        labelRes = R.string.feature_reporting,
        route = "reporting"
    ),
    NavigationItem(
        icon = Icons.Default.Settings,
        labelRes = R.string.feature_system_settings,
        route = "system_settings"
    ),
)

internal val NavigationRailItems = listOf(
    NavigationItem(
        icon = Icons.Default.Dashboard,
        labelRes = R.string.feature_dashboard,
        route = "dashboard"
    ),
    NavigationItem(
        icon = Icons.Default.Dataset,
        labelRes = R.string.feature_datasets,
        route = "datasets"
    ),
    NavigationItem(
        icon = Icons.Default.FolderShared,
        labelRes = R.string.feature_shares,
        route = "shares"
    ),
    NavigationItem(
        icon = Icons.Default.Security,
        labelRes = R.string.feature_data_protection,
        route = "data_protection"
    ),
    NavigationItem(
        icon = Icons.Default.Lan,
        labelRes = R.string.feature_network,
        route = "network"
    ),
    NavigationItem(
        icon = Icons.Default.Key,
        labelRes = R.string.feature_credentials,
        route = "credentials"
    ),
    NavigationItem(
        icon = Icons.Default.Apps,
        labelRes = R.string.feature_apps,
        route = "apps"
    ),
    NavigationItem(
        icon = Icons.Default.Settings,
        labelRes = R.string.feature_system_settings,
        route = "system_settings"
    ),
)

internal val BottomNavigationItems = listOf(
    NavigationItem(
        icon = Icons.Default.Dashboard,
        labelRes = R.string.feature_dashboard,
        route = "dashboard"
    ),
    NavigationItem(
        icon = Icons.Default.Storage,
        labelRes = R.string.feature_storage,
        route = "storage"
    ),
    NavigationItem(
        icon = Icons.Default.FolderShared,
        labelRes = R.string.feature_shares,
        route = "shares"
    ),
    NavigationItem(
        icon = Icons.Default.Lan,
        labelRes = R.string.feature_network,
        route = "network"
    ),
    NavigationItem(
        icon = Icons.Default.Apps,
        labelRes = R.string.feature_apps,
        route = "apps"
    ),
)

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

    val navController = rememberNavController()
    val currentBackstackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination by remember {
        derivedStateOf {
            val currentRoute = currentBackstackEntry?.destination?.parent?.route
            NavigationDrawerItems.firstOrNull { it.route == currentRoute }
        }
    }

    var isPowerOptionsVisible by rememberSaveable { mutableStateOf(false) }

    ProvideNavigationItems(
        selectedNavigationItem = selectedDestination,
        bottomNavigationItems = BottomNavigationItems,
        navigationRailItems = NavigationRailItems,
        navigationDrawerItems = NavigationDrawerItems
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
