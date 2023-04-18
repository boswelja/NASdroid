package com.boswelja.truemanager.reporting

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.reportingGraph(route: String) {
    navigation(startDestination = "overview", route = route) {
        composable("overview") {
            ReportingOverviewScreen()
        }
    }
}
