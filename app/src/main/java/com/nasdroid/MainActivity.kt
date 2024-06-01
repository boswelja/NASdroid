package com.nasdroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nasdroid.auth.ui.authNavigation
import com.nasdroid.design.NasDroidTheme

/**
 * The main entrypoint of the app. See [MainScreen] for content.
 */
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = this)
            NasDroidTheme {
                AppContent(windowSizeClass)
            }
        }
    }
}

/**
 * The main app content, including authentication and post-auth content.
 */
@Composable
fun AppContent(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    NavHost(navController, "auth") {
        authNavigation(
            navController = navController,
            route = "auth",
            windowSizeClass = windowSizeClass,
            onLoginSuccess = {
                navController.navigate("content") {
                    popUpTo("auth") {
                        inclusive = true
                    }
                }
            }
        )
        composable("content") {
            MainScreen(
                onLogOut = {
                    navController.navigate("auth") {
                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
                },
                windowSizeClass = windowSizeClass
            )
        }
    }
}
