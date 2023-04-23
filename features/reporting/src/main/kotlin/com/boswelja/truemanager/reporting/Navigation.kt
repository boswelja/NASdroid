package com.boswelja.truemanager.reporting

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.reportingGraph(route: String, modifier: Modifier = Modifier) {
    navigation(startDestination = "overview", route = route) {
        composable("overview") {
            ReportingOverviewScreen(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(32.dp)
            )
        }
    }
}
