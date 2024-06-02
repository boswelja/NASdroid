package com.nasdroid.apps.ui.installed

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nasdroid.apps.ui.installed.details.InstalledAppDetailsScreen
import com.nasdroid.apps.ui.installed.details.InstalledAppDetailsViewModel
import com.nasdroid.apps.ui.installed.overview.InstalledAppsOverviewScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun InstalledAppsListDetailContent(
    modifier: Modifier = Modifier,
    detailsViewModel: InstalledAppDetailsViewModel = koinViewModel(),
) {
    val selectedAppName by detailsViewModel.appName.collectAsState()
    val navigator = rememberListDetailPaneScaffoldNavigator<String>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                InstalledAppsOverviewScreen(
                    onAppClick = { navigator.navigateTo(ThreePaneScaffoldRole.Secondary, selectedAppName) },
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                )
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let {
                    InstalledAppDetailsScreen(
                        navigateUp = { navigator.navigateBack() },
                        modifier = Modifier.fillMaxSize(),
                        viewModel = detailsViewModel
                    )
                }
            }
        },
        modifier = modifier
    )
}
