package com.boswelja.truemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.boswelja.truemanager.auth.ui.authNavigation
import com.boswelja.truemanager.reporting.reportingGraph
import com.boswelja.truemanager.ui.theme.TrueManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrueManagerTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold {
        NavHost(
            navController = navController,
            startDestination = "auth",
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            authNavigation(
                navController,
                "auth",
                onLoginSuccess = {
                    navController.navigate("reporting") {
                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
                }
            )
            reportingGraph("reporting")
        }
    }
}
