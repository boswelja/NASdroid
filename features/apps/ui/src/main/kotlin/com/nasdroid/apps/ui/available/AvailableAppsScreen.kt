package com.nasdroid.apps.ui.available

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
import com.boswelja.menuprovider.MenuItem
import com.boswelja.menuprovider.ProvideMenuItems
import com.nasdroid.apps.ui.R
import com.nasdroid.apps.ui.available.item.AvailableAppItem
import org.koin.androidx.compose.getViewModel

/**
 * A screen for displaying and allowing installation of apps available from catalogs on the system.
 */
@Composable
fun AvailableAppsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: AvailableAppsViewModel = getViewModel()
) {
    ProvideMenuItems(
        MenuItem(
            label = stringResource(R.string.menu_item_refresh),
            imageVector = Icons.Default.Refresh,
            onClick = viewModel::refresh,
            isImportant = true
        )
    )

    val availableApps by viewModel.availableApps.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(240.dp),
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = availableApps,
            key = { it.name }
        ) { availableApp ->
            AvailableAppItem(
                availableApp = availableApp,
                onInstallClicked = {},
            )
        }
    }
}
