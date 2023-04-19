package com.boswelja.truemanager.auth.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authNavigation(
    route: String,
    onLoginSuccess: () -> Unit
) {
    navigation(
        startDestination = "login",
        route = route
    ) {
        composable("login") {
            AuthScreen(
                onLoginSuccess,
                Modifier.fillMaxSize(),
                PaddingValues(32.dp)
            )
        }
    }
}
