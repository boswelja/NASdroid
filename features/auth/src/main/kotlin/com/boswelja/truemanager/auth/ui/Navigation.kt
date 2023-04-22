package com.boswelja.truemanager.auth.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.boswelja.truemanager.auth.ui.addserver.AuthScreen
import com.boswelja.truemanager.auth.ui.serverselect.SelectServerScreen
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authNavigation(
    navController: NavHostController,
    route: String,
    onLoginSuccess: () -> Unit
) {
    navigation(
        startDestination = "picker",
        route = route,
        enterTransition = { slideInHorizontally { it / 2 } + fadeIn() },
        exitTransition = { slideOutHorizontally() + fadeOut() },
        popEnterTransition = { slideInHorizontally() + fadeIn() },
        popExitTransition = { slideOutHorizontally { it / 2 } + fadeOut() }
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
