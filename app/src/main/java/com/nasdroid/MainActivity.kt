package com.nasdroid

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nasdroid.auth.ui.authNavigation
import com.nasdroid.design.NasDroidTheme

/**
 * The main entrypoint of the app. See [MainScreen] for content.
 */
class MainActivity : ComponentActivity() {

    private val windowInsetsController: WindowInsetsControllerCompat by lazy {
        WindowInsetsControllerCompat(window, window.decorView)
    }

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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // This is a workaround for not recreating the Activity when uiMode changes leading to the
        // status bar not changing between light and dark correctly.
        val currentNightMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> windowInsetsController.isAppearanceLightStatusBars = false
            Configuration.UI_MODE_NIGHT_UNDEFINED,
            Configuration.UI_MODE_NIGHT_NO -> windowInsetsController.isAppearanceLightStatusBars = true
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
            },
            modifier = Modifier.fillMaxSize()
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
            )
        }
    }
}
