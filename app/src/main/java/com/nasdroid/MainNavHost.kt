package com.nasdroid

import androidx.compose.foundation.layout.PaddingValues
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
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard",
        modifier = modifier
    ) {
        dashboardGraph(
            route = "dashboard",
            contentPadding = contentPadding,
            navController = navController
        )
        storageGraph(
            navController = navController,
            route = "storage",
        )
        appsGraph(
            navController = navController,
            route = "apps",
        )
        reportingGraph(
            route = "reporting",
            contentPadding = contentPadding,
            navController = navController
        )
    }
}
