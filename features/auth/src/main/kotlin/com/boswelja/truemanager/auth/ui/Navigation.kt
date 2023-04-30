package com.boswelja.truemanager.auth.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.boswelja.truemanager.auth.ui.addserver.AuthScreen
import com.boswelja.truemanager.auth.ui.serverselect.SelectServerScreen

/**
 * Registers a nested navigation graph for the Auth feature.
 */
fun NavGraphBuilder.authNavigation(
    navController: NavHostController,
    route: String,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    navigation(
        startDestination = "picker",
        route = route
    ) {
        composable("login") {
            AuthScreen(
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
                contentPadding = contentPadding
            )
        }
    }
}
