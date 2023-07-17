package com.boswelja.truemanager.apps.ui.installed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel

/**
 * A screen for displaying and allowing management of apps installed on the system.
 */
@Composable
fun InstalledAppsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: InstalledAppsViewModel = getViewModel()
) {
    val installedApps by viewModel.installedApps.collectAsState()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(240.dp),
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        installedApps?.let {
            items(
                items = it,
            ) { applicationOverview ->
                ApplicationOverviewItem(applicationOverview = applicationOverview)
            }
        }
    }
}
