package com.nasdroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SettingsPower
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nasdroid.apps.ui.appsGraph
import com.nasdroid.auth.ui.authNavigation
import com.nasdroid.auth.ui.selector.DrawerServerSelector
import com.nasdroid.dashboard.ui.dashboardGraph
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.design.NasDroidTheme
import com.nasdroid.navigation.NavigationDrawerLayout
import com.nasdroid.power.ui.PowerOptionsDialog
import com.nasdroid.reporting.ui.reportingGraph
import com.nasdroid.storage.ui.storageGraph
import com.nasdroid.ui.navigation.TopLevelDestination

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
