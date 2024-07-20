package com.nasdroid.apps.ui

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.nasdroid.apps.ui.discover.DiscoverAppsScreen
import com.nasdroid.apps.ui.discover.details.AvailableAppDetailsScreen
import com.nasdroid.apps.ui.installed.InstalledAppsScreen
import com.nasdroid.apps.ui.installed.overview.logs.LogsScreen

/**
 * Registers a nested navigation graph for the Apps feature.
 */
fun NavGraphBuilder.appsGraph(
    navController: NavController,
    route: String,
    modifier: Modifier = Modifier,
) {
    navigation(
        startDestination = "installed",
        route = route
    ) {
        composable("installed") {
            InstalledAppsScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                },
                modifier = modifier,
            )
        }
        composable("discover") {
            DiscoverAppsScreen(
                onAppClick = { appId, appCatalog, appTrain ->
                    navController.navigate("discover/$appCatalog/$appTrain/$appId")
                },
                navigateBack = { navController.popBackStack() },
                modifier = modifier,
            )
        }
        composable(
            route = "discover/{catalog}/{train}/{id}",
            arguments = listOf(
                navArgument("catalog") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("train") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("id") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            AvailableAppDetailsScreen(
                onNavigateBack = navController::popBackStack,
                modifier = modifier
            )
        }
        composable(
            route = "logs/{appName}",
            arguments = listOf(
                navArgument("appName") {
                    nullable = false
                    type = NavType.StringType
                }
            )
        ) {
            LogsScreen(modifier = modifier)
        }
    }
}
