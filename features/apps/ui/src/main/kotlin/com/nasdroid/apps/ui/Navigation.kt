package com.nasdroid.apps.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
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
import com.nasdroid.apps.ui.installed.details.InstalledAppDetailsScreen
import com.nasdroid.apps.ui.installed.overview.logs.LogsScreen

/**
 * Registers a nested navigation graph for the Apps feature.
 */
fun NavGraphBuilder.appsGraph(
    windowSizeClass: WindowSizeClass,
    navController: NavController,
    route: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    navigation(
        startDestination = "installed",
        route = route
    ) {
        composable("installed") {
            InstalledAppsScreen(
                windowSizeClass = windowSizeClass,
                onNavigate = { route ->
                    navController.navigate(route)
                },
                modifier = modifier,
                contentPadding = contentPadding,
            )
        }
        composable("discover") {
            DiscoverAppsScreen(
                modifier = modifier,
                contentPadding = contentPadding,
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
                modifier = modifier,
                contentPadding = contentPadding
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
            LogsScreen(
                modifier = modifier,
                contentPadding = contentPadding
            )
        }
        composable(
            route = "details/{appName}",
            arguments = listOf(
                navArgument("appName") {
                    nullable = false
                    type = NavType.StringType
                }
            )
        ) {
            InstalledAppDetailsScreen(
                navigateUp = {
                    navController.popBackStack()
                },
                modifier = modifier,
                contentPadding = contentPadding
            )
        }
    }
}
