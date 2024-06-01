package com.nasdroid.auth.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nasdroid.auth.ui.register.RegisterServerScreen
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
) {
    navigation(
        startDestination = "picker",
        route = route
    ) {
        composable("picker") {
            SelectServerScreen(
                onLoginSuccess = onLoginSuccess,
                onAddServer = {
                    navController.navigate("addServer")
                },
                modifier = modifier,
                windowSizeClass = windowSizeClass,
            )
        }
        composable("addServer") {
            RegisterServerScreen(
                onServerRegistered = {
                    navController.navigate("picker") {
                        popUpTo("picker")
                    }
                },
                onNavigateBack = { navController.popBackStack() },
                modifier = modifier,
                windowSizeClass = windowSizeClass,
            )
        }
    }
}
