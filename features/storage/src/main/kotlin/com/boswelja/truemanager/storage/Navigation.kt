package com.boswelja.truemanager.storage

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.boswelja.truemanager.storage.overview.StorageOverviewScreen

fun NavGraphBuilder.storageGraph(
    route: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    navigation(startDestination = "overview", route = route) {
        composable("overview") {
            StorageOverviewScreen(
                modifier = modifier,
                contentPadding = contentPadding,
            )
        }
    }
}
