package com.nasdroid.auth.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nasdroid.auth.ui.addserver.AddServerScreen
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
        composable("login") {
            AddServerScreen(
                onLoginSuccess = onLoginSuccess,
                modifier = modifier,
                contentPadding = contentPadding
            )
        }
        composable("picker") {
            SelectServerScreen(
                onLoginSuccess = onLoginSuccess,
                onAddServer = {
                    navController.navigate("login")
                },
                modifier = modifier,
                contentPadding = contentPadding,
                windowSizeClass = windowSizeClass,
            )
        }
    }
}
