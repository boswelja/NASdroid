package com.nasdroid.apps.ui.installed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material.icons.twotone.Image
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nasdroid.apps.ui.R
import com.nasdroid.apps.ui.installed.details.InstalledAppDetailsScreen
import com.nasdroid.apps.ui.installed.overview.InstalledAppsOverviewScreen
import com.nasdroid.navigation.NavigationSuiteScaffold

/**
 * An adaptive screen that will display [InstalledAppsOverviewScreen] on all screen sizes, and
 * [InstalledAppDetailsScreen] on larger devices in a split view.
 */
@Composable
fun InstalledAppsScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationSuiteScaffold(
        title = { Text("Installed Apps") },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onNavigate("discover") }
            ) {
                Icon(Icons.Default.GetApp, contentDescription = null)
                Text("Discover Apps")
            }
        },
        onNavigate = onNavigate,
        modifier = modifier
    ) { contentPadding ->
        InstalledAppsListDetailContent(Modifier.padding(contentPadding))
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
