package com.nasdroid.auth.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.nasdroid.auth.ui.register.addserver.AddServerScreen
import com.nasdroid.auth.ui.register.auth.AuthServerScreen
import com.nasdroid.auth.ui.register.find.FindServerScreen
import com.nasdroid.auth.ui.serverselect.SelectServerScreen

/**
 * Registers a nested navigation graph for the Auth feature.
 */
fun NavGraphBuilder.authNavigation(
    navController: NavHostController,
    route: String,
    windowSizeClass: WindowSizeClass,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    navigation(
        startDestination = "picker",
        route = route
    ) {
        composable("picker") {
            SelectServerScreen(
                onLoginSuccess = onLoginSuccess,
                onAddServer = {
                    navController.navigate("find_server")
                },
                modifier = modifier,
                contentPadding = contentPadding,
                windowSizeClass = windowSizeClass,
            )
        }
        composable("find_server") {
            FindServerScreen(
                onServerFound = {
                    navController.navigate("auth_server/$it")
                },
                modifier = modifier,
                contentPadding = contentPadding,
            )
        }
        composable(
            "auth_server/{address}",
            arguments = listOf(
                navArgument("address") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            AuthServerScreen(
                onLoginSuccess = {
                    navController.navigate("picker") {
                        popUpTo("picker")
                    }
                },
                modifier = modifier,
                contentPadding = contentPadding,
            )
        }
        composable("add_server") {
            AddServerScreen(
                onLoginSuccess = onLoginSuccess,
                modifier = modifier,
                contentPadding = contentPadding
            )
        }
    }
}
