package com.nasdroid

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.nasdroid.apps.ui.appsGraph
import com.nasdroid.auth.ui.authNavigation
import com.nasdroid.dashboard.ui.dashboardGraph
import com.nasdroid.reporting.ui.reportingGraph
import com.nasdroid.storage.ui.storageGraph
import com.nasdroid.ui.navigation.TopLevelDestination

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
        startDestination = TopLevelDestination.Dashboard.getRoute(),
        modifier = modifier
    ) {
        destinations.forEach { destination ->
            when (destination) {
                TopLevelDestination.Dashboard -> dashboardGraph(
                    route = destination.getRoute(),
                    contentPadding = contentPadding
                )
                TopLevelDestination.Storage -> storageGraph(
                    windowSizeClass = windowSizeClass,
                    navController = navController,
                    route = destination.getRoute(),
                    contentPadding = contentPadding
                )
                TopLevelDestination.Datasets -> {}
                TopLevelDestination.Shares -> {}
                TopLevelDestination.DataProtection -> {}
                TopLevelDestination.Network -> {}
                TopLevelDestination.Credentials -> {}
                TopLevelDestination.Virtualization -> {}
                TopLevelDestination.Apps -> appsGraph(
                    windowSizeClass = windowSizeClass,
                    navController = navController,
                    route = destination.getRoute(),
                    contentPadding = contentPadding
                )
                TopLevelDestination.Reporting -> reportingGraph(
                    route = destination.getRoute(),
                    contentPadding = contentPadding
                )
                TopLevelDestination.SystemSettings -> {}
            }
        }
    }
}

internal fun TopLevelDestination.getRoute(): String {
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
