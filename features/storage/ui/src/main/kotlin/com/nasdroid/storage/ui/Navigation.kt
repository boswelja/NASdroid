package com.nasdroid.storage.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nasdroid.storage.ui.pools.overview.StorageOverviewScreen

/**
 * Registers a nested navigation graph for the Storage feature.
 */
fun NavGraphBuilder.storageGraph(
    route: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    navigation(startDestination = "poolsOverview", route = route) {
        composable("poolsOverview") {
            StorageOverviewScreen(
                modifier = modifier,
                contentPadding = contentPadding,
            )
        }
    }
}
