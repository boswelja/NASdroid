package com.boswelja.truemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.boswelja.truemanager.auth.ui.authNavigation
import com.boswelja.truemanager.reporting.reportingGraph
import com.boswelja.truemanager.ui.navigation.TopLevelDestination
import com.boswelja.truemanager.ui.navigation.TopLevelNavigation
import com.boswelja.truemanager.ui.theme.TrueManagerTheme

/**
 * The main entrypoint of the app. See [MainScreen] for content.
 */
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = this)
            TrueManagerTheme {
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
    val destinations = remember {
        TopLevelDestination.values().toList()
    }
    val selectedDestination by remember {
        derivedStateOf {
            val currentRoute = navController.currentDestination?.route
            destinations.firstOrNull { it.route == currentRoute }
        }
    }
    val isNavigationVisible by remember {
        derivedStateOf {
            val currentRoute = navController.currentDestination?.route.orEmpty()
            destinations.any { destination ->
                currentRoute.contains(destination.route)
            }
        }
    }

    TopLevelNavigation(
        windowSizeClass = windowSizeClass,
        selectedDestination = selectedDestination,
        destinations = destinations,
        navigateTo = { navController.navigate(it.route) },
        navigationVisible = isNavigationVisible,
        canNavigateBack = navController.previousBackStackEntry != null,
        navigateBack = navController::navigateUp
    ) {
        NavHost(
            navController = navController,
            startDestination = "auth",
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            authNavigation(
                navController,
                "auth",
                onLoginSuccess = {
                    navController.navigate("reporting") {
                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
                }
            )

            destinations.forEach { destination ->
                when (destination) {
                    TopLevelDestination.Dashboard -> {}
                    TopLevelDestination.Storage -> {}
                    TopLevelDestination.Datasets -> {}
                    TopLevelDestination.Shares -> {}
                    TopLevelDestination.DataProtection -> {}
                    TopLevelDestination.Network -> {}
                    TopLevelDestination.Credentials -> {}
                    TopLevelDestination.Virtualization -> {}
                    TopLevelDestination.Apps -> {}
                    TopLevelDestination.Reporting -> reportingGraph(destination.route)
                    TopLevelDestination.SystemSettings -> {}
                }
            }
        }
    }
}
