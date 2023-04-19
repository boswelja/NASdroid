package com.boswelja.truemanager.auth

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authNavigation(route: String) {
    navigation(
        startDestination = "login",
        route = route
    ) {
        composable("login") {
            AuthScreen(
                Modifier.fillMaxSize().padding(32.dp)
            )
        }
    }
}
