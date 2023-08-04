package com.boswelja.truemanager.apps.ui.installed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.apps.ui.R
import com.boswelja.truemanager.apps.ui.installed.item.ApplicationOverviewItem
import com.boswelja.truemanager.apps.ui.installed.item.InstalledAppAction
import com.boswelja.truemanager.core.menuprovider.MenuItem
import com.boswelja.truemanager.core.menuprovider.ProvideMenuItems
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
    ProvideMenuItems(
        MenuItem(
            label = stringResource(R.string.menu_item_refresh),
            imageVector = Icons.Default.Refresh,
            onClick = viewModel::refresh,
            isImportant = true
        )
    )

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
                ApplicationOverviewItem(
                    installedApplication = applicationOverview,
                    onActionClicked = {
                        when (it) {
                            InstalledAppAction.UPGRADE -> TODO()
                            InstalledAppAction.ROLL_BACK -> TODO()
                            InstalledAppAction.EDIT -> TODO()
                            InstalledAppAction.SHELL -> TODO()
                            InstalledAppAction.LOGS -> TODO()
                            InstalledAppAction.DELETE -> TODO()
                            InstalledAppAction.START -> TODO()
                            InstalledAppAction.STOP -> TODO()
                            InstalledAppAction.WEB_PORTAL -> TODO()
                        }
                    }
                )
            }
        }
    }
}
