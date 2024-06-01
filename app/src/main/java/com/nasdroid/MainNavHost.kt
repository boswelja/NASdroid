package com.nasdroid

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.nasdroid.apps.ui.appsGraph
import com.nasdroid.dashboard.ui.dashboardGraph
import com.nasdroid.reporting.ui.reportingGraph
import com.nasdroid.storage.ui.storageGraph

/**
 * Shows a [NavHost] with routes for the main app content.
 */
@Composable
fun MainNavHost(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard",
        modifier = modifier
    ) {
        dashboardGraph(route = "dashboard", contentPadding = contentPadding)
        storageGraph(
            navController = navController,
            windowSizeClass = windowSizeClass,
            route = "storage",
            contentPadding = contentPadding
        )
        appsGraph(
            windowSizeClass = windowSizeClass,
            navController = navController,
            route = "apps",
            contentPadding = contentPadding
        )
        reportingGraph(
            route = "reporting",
            contentPadding = contentPadding
        )
    }
}
