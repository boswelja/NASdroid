package com.nasdroid.storage.ui

import androidx.compose.foundation.layout.PaddingValues
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
    contentPadding: PaddingValues = PaddingValues()
) {
    navigation(startDestination = "poolsOverview", route = route) {
        composable("poolsOverview") {
            StoragePoolsScreen(
                onNavigate = { navController.navigate(it) },
                windowSizeClass = windowSizeClass,
                modifier = modifier,
                contentPadding = contentPadding,
            )
        }
        composable(
            route = "poolDetails/{id}",
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
