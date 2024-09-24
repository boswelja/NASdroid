package com.nasdroid.storage.ui

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.nasdroid.storage.ui.pools.StorageOverviewScreen
import com.nasdroid.storage.ui.pools.details.PoolDetailsScreen

/**
 * Registers a nested navigation graph for the Storage feature.
 */
fun NavGraphBuilder.storageGraph(
    navController: NavController,
    route: String,
    modifier: Modifier = Modifier,
) {
    navigation(startDestination = "pools", route = route) {
        composable("pools") { _ ->
            StorageOverviewScreen(
                onNavigate = { navController.navigate(it) },
                modifier = modifier,
            )
        }
        composable(
            route = "pool/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) {
            PoolDetailsScreen(
                onNavigateBack = navController::popBackStack,
                modifier = modifier
            )
        }
    }
}
