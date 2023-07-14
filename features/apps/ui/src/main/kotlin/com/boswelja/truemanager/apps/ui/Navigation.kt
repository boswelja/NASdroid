package com.boswelja.truemanager.apps.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

/**
 * Registers a nested navigation graph for the Apps feature.
 */
fun NavGraphBuilder.appsGraph(
    route: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    navigation(
        startDestination = "overview",
        route = route
    ) {
        composable("overview") {
            AppsScreen(modifier = modifier, contentPadding = contentPadding)
        }
    }
}
