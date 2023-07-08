package com.boswelja.truemanager.dashboard.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.boswelja.truemanager.dashboard.ui.overview.DashboardOverviewScreen

/**
 * Registers a nested navigation graph for the Dashboard feature.
 */
fun NavGraphBuilder.dashboardGraph(
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
                modifier = modifier,
                contentPadding = contentPadding,
            )
        }
    }
}
