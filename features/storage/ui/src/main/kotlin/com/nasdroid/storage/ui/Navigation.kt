package com.nasdroid.storage.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.nasdroid.storage.ui.pools.StoragePoolsScreen

/**
 * Registers a nested navigation graph for the Storage feature.
 */
fun NavGraphBuilder.storageGraph(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    route: String,
    modifier: Modifier = Modifier,
) {
    navigation(startDestination = "pools", route = route) {
        composable("pools") { _ ->
            StoragePoolsScreen(
                onNavigate = { navController.navigate(it) },
                windowSizeClass = windowSizeClass,
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
            // TODO Details screen
        }
    }
}
