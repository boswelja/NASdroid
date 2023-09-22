package com.nasdroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nasdroid.apps.ui.appsGraph
import com.nasdroid.auth.ui.authNavigation
import com.nasdroid.dashboard.ui.dashboardGraph
import com.nasdroid.reporting.reportingGraph
import com.nasdroid.storage.ui.storageGraph
import com.nasdroid.ui.navigation.TopLevelDestination
import com.nasdroid.ui.navigation.TopLevelNavigation
import com.nasdroid.ui.theme.NasDroidTheme

/**
 * The main entrypoint of the app. See [MainScreen] for content.
 */
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = this)
            NasDroidTheme {
                MainScreen(windowSizeClass)
            }
        }
    }
}

/**
 * The main content of the app. This Composable displays navigation-related elements, and contains a
 * NavHost for features to display their own screens.
 */
@Composable
fun MainScreen(
    windowSizeClass: WindowSizeClass
) {
    val navController = rememberNavController()
    val currentBackstackEntry by navController.currentBackStackEntryAsState()
    val destinations = remember {
        TopLevelDestination.entries
    }
    val selectedDestination by remember {
        derivedStateOf {
            val currentRoute = currentBackstackEntry?.destination?.parent?.route
            destinations.firstOrNull { it.getRoute() == currentRoute }
        }
    }
    val isNavigationVisible by remember {
        derivedStateOf {
            val currentRoute = currentBackstackEntry?.destination?.parent?.route
            destinations.any { it.getRoute() == currentRoute }
        }
    }

    TopLevelNavigation(
        windowSizeClass = windowSizeClass,
        selectedDestination = selectedDestination,
        navigateTo = {
            navController.navigate(it.getRoute()) {
                val currentRoute = requireNotNull(currentBackstackEntry?.destination?.route)
                popUpTo(currentRoute) {
                    inclusive = true
                }
            }
        },
        navigationVisible = isNavigationVisible,
        canNavigateBack = navController.previousBackStackEntry != null,
        navigateBack = navController::navigateUp
    ) {
        MainNavHost(
            navController = navController,
            destinations = destinations,
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
            windowSizeClass = windowSizeClass,
        )
    }
}

/**
 * Shows a [NavHost] with routes for all given [destinations].
 */
@Composable
fun MainNavHost(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    destinations: List<TopLevelDestination>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        authNavigation(
            navController,
            "auth",
            onLoginSuccess = {
                navController.navigate(TopLevelDestination.Dashboard.getRoute()) {
                    popUpTo("auth") {
                        inclusive = true
                    }
                }
            },
            modifier = modifier,
            contentPadding = contentPadding,
            windowSizeClass = windowSizeClass,
        )

        destinations.forEach { destination ->
            when (destination) {
                TopLevelDestination.Dashboard -> dashboardGraph(destination.getRoute(), modifier, contentPadding)
                TopLevelDestination.Storage -> storageGraph(destination.getRoute(), modifier, contentPadding)
                TopLevelDestination.Datasets -> {}
                TopLevelDestination.Shares -> {}
                TopLevelDestination.DataProtection -> {}
                TopLevelDestination.Network -> {}
                TopLevelDestination.Credentials -> {}
                TopLevelDestination.Virtualization -> {}
                TopLevelDestination.Apps -> appsGraph(navController, destination.getRoute(), modifier, contentPadding)
                TopLevelDestination.Reporting -> reportingGraph(destination.getRoute(), modifier, contentPadding)
                TopLevelDestination.SystemSettings -> {}
            }
        }
    }
}

fun TopLevelDestination.getRoute(): String {
    return when (this) {
        TopLevelDestination.Dashboard -> "dashboard"
        TopLevelDestination.Storage -> "storage"
        TopLevelDestination.Datasets -> "datasets"
        TopLevelDestination.Shares -> "shares"
        TopLevelDestination.DataProtection -> "data_protection"
        TopLevelDestination.Network -> "network"
        TopLevelDestination.Credentials -> "credentials"
        TopLevelDestination.Virtualization -> "virtualization"
        TopLevelDestination.Apps -> "apps"
        TopLevelDestination.Reporting -> "reporting"
        TopLevelDestination.SystemSettings -> "system_settings"
    }
}