package com.nasdroid.dashboard.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nasdroid.dashboard.ui.overview.DashboardOverviewScreen

/**
 * Registers a nested navigation graph for the Dashboard feature.
 */
fun NavGraphBuilder.dashboardGraph(
    navController: NavController,
    route: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    navigation(
        startDestination = "overview",
        route = route
    ) {
        composable("overview") {
            DashboardOverviewScreen(
                navController = navController,
                modifier = modifier,
                contentPadding = contentPadding,
            )
        }
    }
}
