package com.nasdroid.apps.ui.installed.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.boswelja.menuprovider.MenuItem
import com.boswelja.menuprovider.ProvideMenuItems
import com.nasdroid.apps.ui.R
import com.nasdroid.apps.ui.installed.overview.item.ApplicationOverviewItem
import org.koin.androidx.compose.koinViewModel

/**
 * A screen for displaying apps installed on the system.
 */
@Composable
fun InstalledAppsOverviewScreen(
    onNavigate: (route: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: InstalledAppsOverviewViewModel = koinViewModel()
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

    var deletingApp by rememberSaveable { mutableStateOf<String?>(null) }

    Box(modifier) {
        LazyColumn(
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            installedApps?.let { apps ->
                items(
                    items = apps,
                    key = { it.name }
                ) { applicationOverview ->
                    ApplicationOverviewItem(
                        installedApplication = applicationOverview,
                        onClick = { /* TODO */ },
                        onAppStartRequest = { viewModel.start(applicationOverview.name) },
                        onAppStopRequest = { viewModel.stop(applicationOverview.name) },
                    )
                }
            }
            item {
                // TODO Don't use Spacer for extended FAB padding
                Spacer(Modifier.height(56.dp))
            }
        }

        ExtendedFloatingActionButton(
            onClick = { onNavigate("discover") },
            modifier = Modifier
                .padding(contentPadding)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Default.GetApp, contentDescription = null)
            Text("Discover Apps")
        }
    }

    deletingApp?.let { appName ->
        DeleteAppDialog(
            appName = appName,
            onConfirm = { viewModel.delete(appName, it) },
            onCancel = { deletingApp = null }
        )
    }
}

@Composable
internal fun DeleteAppDialog(
    appName: String,
    onConfirm: (deleteUnusedImages: Boolean) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var deleteImages by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            TextButton(onClick = { onConfirm(deleteImages) }) {
                Text(stringResource(R.string.app_delete_continue))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.app_delete_cancel))
            }
        },
        icon = {
            Icon(Icons.Default.Delete, null)
        },
        title = {
            Text(stringResource(R.string.app_delete_title, appName))
        },
        text = {
            Row(
                modifier = Modifier.clickable { deleteImages = !deleteImages },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(checked = deleteImages, onCheckedChange = null)
                Text(stringResource(R.string.app_delete_data))
            }
        },
        modifier = modifier
    )
}
