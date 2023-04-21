package com.boswelja.truemanager.reporting

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.reportingGraph(route: String) {
    navigation(startDestination = "overview", route = route) {
        composable("overview") {
            ReportingOverviewScreen(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(32.dp)
            )
        }
    }
}
