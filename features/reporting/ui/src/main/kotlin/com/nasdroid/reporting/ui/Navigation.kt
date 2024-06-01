package com.nasdroid.reporting.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nasdroid.reporting.ui.overview.ReportingOverviewScreen

/**
 * Registers a nested navigation graph for the Reporting feature.
 */
fun NavGraphBuilder.reportingGraph(
    navController: NavController,
    route: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    navigation(startDestination = "overview", route = route) {
        composable("overview") {
            ReportingOverviewScreen(
                navController = navController,
                modifier = modifier,
                contentPadding = contentPadding
            )
        }
    }
}
