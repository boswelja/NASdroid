package com.nasdroid.auth.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nasdroid.auth.ui.register.addserver.AddServerScreen
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
                    navController.navigate("add_server")
                },
                modifier = modifier,
                contentPadding = contentPadding,
                windowSizeClass = windowSizeClass,
            )
        }
        composable("find_server") {
            FindServerScreen(
                onServerFound = {

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
