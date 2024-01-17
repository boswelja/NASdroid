package com.nasdroid.apps.ui.installed.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nasdroid.apps.logic.installed.InstalledAppDetails
import com.nasdroid.apps.ui.R
import com.nasdroid.design.MaterialThemeExt
import org.koin.androidx.compose.koinViewModel

/**
 * A screen that displays detailed information about a single installed application.
 */
@Composable
fun InstalledAppDetailsScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: InstalledAppDetailsViewModel = koinViewModel()
) {
    val appDetails by viewModel.appDetails.collectAsState()
    var showRollbackDialog by rememberSaveable(appDetails) { mutableStateOf(false) }

    AnimatedContent(
        targetState = appDetails,
        label = "Installed App Details Content",
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) {
        if (it != null) {
            InstalledAppDetailsContent(
                installedAppDetails = it,
                onDeleteClick = {
                    viewModel.tryDeleteApp(true)
                    navigateUp()
                },
                onRollbackClick = {
                    showRollbackDialog = true
                },
                modifier = modifier,
                contentPadding = contentPadding,
            )
        } else {
            Box(modifier) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
    if (showRollbackDialog) {
        val rollbackOptions by viewModel.rollbackOptions.collectAsState()
        RollbackAppDialog(
            availableVersions = rollbackOptions?.availableVersions.orEmpty(),
            onConfirm = { version, rollbackSnapshots ->
                viewModel.tryRollBackApp(version, rollbackSnapshots)
                showRollbackDialog = false
            },
            onDismiss = { showRollbackDialog = false },
            loading = rollbackOptions == null
        )
    }
}

/**
 * Displays the contents of [InstalledAppDetailsScreen], when there are app details to display.
 */
@Composable
fun InstalledAppDetailsContent(
    installedAppDetails: InstalledAppDetails,
    onDeleteClick: () -> Unit,
    onRollbackClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    var isShowDeleteDialog by rememberSaveable {
        mutableStateOf(false)
    }
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(240.dp),
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.medium),
        verticalItemSpacing = MaterialThemeExt.paddings.medium
    ) {
        item {
            ApplicationInfo(
                installedAppDetails = installedAppDetails,
                onEditClick = { /* TODO */ },
                onRollBackClick = onRollbackClick,
                onDeleteClick = {
                    isShowDeleteDialog = true
                }
            )
        }
        installedAppDetails.notes?.let {
            item {
                ApplicationNotes(note = it)
            }
        }
    }
    if (isShowDeleteDialog) {
        DeleteAppDialog(
            appName = installedAppDetails.name,
            onConfirm = onDeleteClick,
            onDismiss = { isShowDeleteDialog = false }
        )
    }
}

@Composable
internal fun DeleteAppDialog(
    appName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.app_delete_continue))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.app_delete_cancel))
            }
        },
        icon = {
            Icon(Icons.Default.Delete, null)
        },
        title = {
            Text(stringResource(R.string.app_delete_title))
        },
        text = {
            Text(stringResource(R.string.app_delete_text, appName))
        },
        modifier = modifier
    )
}
