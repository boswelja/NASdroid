package com.boswelja.truemanager.apps.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.boswelja.truemanager.apps.ui.installed.logs.LogsScreen

/**
 * Registers a nested navigation graph for the Apps feature.
 */
fun NavGraphBuilder.appsGraph(
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
            AppsScreen(
                onShowLogs = {
                    navController.navigate("logs")
                },
                modifier = modifier,
                contentPadding = contentPadding
            )
        }
        composable(
            route = "logs",
            arguments = listOf(
                navArgument("appName") {
                    nullable = false
                    type = NavType.StringType
                }
            )
        ) {
            LogsScreen(
                modifier = modifier,
                contentPadding = contentPadding
            )
        }
        composable(
            route = "logs",
            arguments = listOf(
                navArgument(name = "appName") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            LogsScreen(modifier = modifier, contentPadding = contentPadding)
        }
    }
}
