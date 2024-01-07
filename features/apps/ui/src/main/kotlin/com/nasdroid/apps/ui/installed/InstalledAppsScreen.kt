package com.nasdroid.apps.ui.installed

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nasdroid.apps.ui.R
import com.nasdroid.apps.ui.installed.details.InstalledAppDetailsScreen
import com.nasdroid.apps.ui.installed.details.InstalledAppDetailsViewModel
import com.nasdroid.apps.ui.installed.overview.InstalledAppsOverviewScreen
import org.koin.androidx.compose.koinViewModel

/**
 * An adaptive screen that will display [InstalledAppsOverviewScreen] on all screen sizes, and
 * [InstalledAppDetailsScreen] on larger devices in a split view.
 */
@Composable
fun InstalledAppsScreen(
    windowSizeClass: WindowSizeClass,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    if (windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium) {
        val detailsViewModel: InstalledAppDetailsViewModel = koinViewModel()
        val selectedAppName by detailsViewModel.appName.collectAsState()
        Row(modifier) {
            InstalledAppsOverviewScreen(
                onAppClick = {
                    detailsViewModel.setAppName(it)
                },
                onNavigate = onNavigate,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentPadding = contentPadding,
            )
            VerticalDivider()
            AnimatedContent(
                targetState = selectedAppName != null,
                label = "Installed app details pane",
                modifier = Modifier
                    .weight(1f)
            ) { hasApp ->
                if (hasApp) {
                    InstalledAppDetailsScreen(
                        navigateUp = { detailsViewModel.setAppName(null) },
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = contentPadding,
                        viewModel = detailsViewModel
                    )
                } else {
                    SelectAppHint(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding)
                    )
                }
            }
        }
    } else {
        InstalledAppsOverviewScreen(
            onAppClick = {
                onNavigate("details/$it")
            },
            onNavigate = onNavigate,
            modifier = modifier,
            contentPadding = contentPadding,
        )
    }
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
