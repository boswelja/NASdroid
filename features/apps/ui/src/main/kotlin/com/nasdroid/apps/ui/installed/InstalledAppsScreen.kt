package com.nasdroid.apps.ui.installed

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nasdroid.apps.ui.R
import com.nasdroid.apps.ui.installed.details.InstalledAppDetailsScreen
import com.nasdroid.apps.ui.installed.details.InstalledAppDetailsViewModel
import com.nasdroid.apps.ui.installed.overview.InstalledAppsOverviewScreen
import com.nasdroid.design.MaterialThemeExt
import org.koin.androidx.compose.koinViewModel

/**
 * An adaptive screen that will display [InstalledAppsOverviewScreen] on all screen sizes, and
 * [InstalledAppDetailsScreen] on larger devices in a split view.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun InstalledAppsScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<String>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane(
                modifier = Modifier.preferredWidth(460.dp)
            ) {
                InstalledAppsOverviewScreen(
                    onAppClick = { navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it) },
                    onNavigate = onNavigate,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let { appName ->
                    // TODO Nope
                    val detailsViewModel: InstalledAppDetailsViewModel = koinViewModel()
                    LaunchedEffect(detailsViewModel, appName) {
                        detailsViewModel.setAppName(appName)
                    }
                    if (navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Hidden) {
                        InstalledAppDetailsScreen(
                            navigateUp = navigator::navigateBack,
                            modifier = Modifier.fillMaxSize(),
                            viewModel = detailsViewModel
                        )
                    } else {
                        InstalledAppDetailsScreen(
                            modifier = Modifier.fillMaxSize(),
                            viewModel = detailsViewModel
                        )
                    }
                } ?: SelectAppHint(Modifier.fillMaxSize())
            }
        },
        modifier = modifier.background(MaterialThemeExt.colorScheme.background)
    )
}

@Composable
internal fun SelectAppHint(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.TwoTone.Image, contentDescription = null, modifier = Modifier.size(120.dp))
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.overview_details_pane_empty),
            textAlign = TextAlign.Center
        )
    }
}
