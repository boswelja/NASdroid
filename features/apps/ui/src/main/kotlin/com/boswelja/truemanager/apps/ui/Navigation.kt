package com.boswelja.truemanager.apps.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.boswelja.truemanager.apps.ui.installed.logs.LogsScreen
import com.boswelja.truemanager.apps.ui.installed.upgrade.AppUpgradeDialog

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
                    navController.navigate("logs/$it")
                },
                onStartUpgrade = {
                    navController.navigate("upgrade/$it")
                },
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
        dialog(
            "upgrade/{appName}",
            arguments = listOf(
                navArgument("appName") {
                    nullable = false
                    type = NavType.StringType
                }
            )
        ) {
            AppUpgradeDialog(
                onDismissRequest = { navController.popBackStack() }
            )
        }
    }
}
