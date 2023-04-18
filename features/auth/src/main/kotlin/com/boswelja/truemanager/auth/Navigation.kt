package com.boswelja.truemanager.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.authNavigation(route: String, onLoginSuccess: () -> Unit) {
    navigation(
        startDestination = "login",
        route = route
    ) {
        composable("login") {
            AuthScreen(onLoginSuccess)
        }
    }
}
