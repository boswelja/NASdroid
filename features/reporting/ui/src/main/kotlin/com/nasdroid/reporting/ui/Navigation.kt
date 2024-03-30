package com.nasdroid.reporting.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nasdroid.reporting.overview.ReportingOverviewScreen

/**
 * Registers a nested navigation graph for the Reporting feature.
 */
fun NavGraphBuilder.reportingGraph(
    route: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    navigation(startDestination = "overview", route = route) {
        composable("overview") {
            ReportingOverviewScreen(
                modifier = modifier,
                contentPadding = contentPadding
            )
        }
    }
}
