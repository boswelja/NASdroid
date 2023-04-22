package com.boswelja.truemanager.auth.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.boswelja.truemanager.auth.ui.addserver.AuthScreen
import com.boswelja.truemanager.auth.ui.serverselect.SelectServerScreen

fun NavGraphBuilder.authNavigation(
    navController: NavHostController,
    route: String,
    onLoginSuccess: () -> Unit
) {
    navigation(
        startDestination = "picker",
        route = route
    ) {
        composable("login") {
            AuthScreen(
                onLoginSuccess = onLoginSuccess,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(32.dp)
            )
        }
        composable("picker") {
            SelectServerScreen(
                onLoginSuccess = onLoginSuccess,
                onAddServer = {
                    navController.navigate("login")
                },
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(32.dp)
            )
        }
    }
}
