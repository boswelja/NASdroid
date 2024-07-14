package com.nasdroid.apps.ui.installed.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import com.nasdroid.navigation.BackNavigationScaffold
import org.koin.androidx.compose.koinViewModel

/**
 * A screen that displays detailed information about a single installed application.
 */
@Composable
fun InstalledAppDetailsScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InstalledAppDetailsViewModel = koinViewModel()
) {
    val appDetails by viewModel.appDetails.collectAsState()
    var isShowRollbackDialog by rememberSaveable(appDetails) { mutableStateOf(false) }

    BackNavigationScaffold(
        title = { Text("App Details") },
        onNavigateBack = navigateUp,
        modifier = modifier,
    ) {
        AnimatedContent(
            targetState = appDetails,
            label = "Installed App Details Content",
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { installedAppDetails ->
            if (installedAppDetails != null) {
                InstalledAppDetailsContent(
                    installedAppDetails = installedAppDetails,
                    onDeleteClick = {
                        viewModel.tryDeleteApp(true)
                        navigateUp()
                    },
                    onRollbackClick = {
                        isShowRollbackDialog = true
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = MaterialThemeExt.paddings.large,
                            vertical = MaterialThemeExt.paddings.medium
                        ),
                    contentPadding = it,
                )
            } else {
                Box(Modifier.fillMaxSize().padding(it)) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
        }
    }

    if (isShowRollbackDialog) {
        val rollbackOptions by viewModel.rollbackOptions.collectAsState()
        RollbackAppDialog(
            availableVersions = rollbackOptions?.availableVersions.orEmpty(),
            onConfirm = { version, rollbackSnapshots ->
                viewModel.tryRollBackApp(version, rollbackSnapshots)
                isShowRollbackDialog = false
            },
            onDismiss = { isShowRollbackDialog = false },
            loading = rollbackOptions == null
        )
    }
}

/**
 * A screen that displays detailed information about a single installed application.
 */
@Composable
fun InstalledAppDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: InstalledAppDetailsViewModel = koinViewModel()
) {
    val appDetails by viewModel.appDetails.collectAsState()
    var isShowRollbackDialog by rememberSaveable(appDetails) { mutableStateOf(false) }

    Scaffold(modifier = modifier) {
        AnimatedContent(
            targetState = appDetails,
            label = "Installed App Details Content",
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { installedAppDetails ->
            if (installedAppDetails != null) {
                InstalledAppDetailsContent(
                    installedAppDetails = installedAppDetails,
                    onDeleteClick = {
                        viewModel.tryDeleteApp(true)
                    },
                    onRollbackClick = {
                        isShowRollbackDialog = true
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = MaterialThemeExt.paddings.large,
                            vertical = MaterialThemeExt.paddings.medium
                        ),
                    contentPadding = it
                )
            } else {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
        }

    }

    if (isShowRollbackDialog) {
        val rollbackOptions by viewModel.rollbackOptions.collectAsState()
        RollbackAppDialog(
            availableVersions = rollbackOptions?.availableVersions.orEmpty(),
            onConfirm = { version, rollbackSnapshots ->
                viewModel.tryRollBackApp(version, rollbackSnapshots)
                isShowRollbackDialog = false
            },
            onDismiss = { isShowRollbackDialog = false },
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
        columns = StaggeredGridCells.Adaptive(320.dp),
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.medium),
        verticalItemSpacing = MaterialThemeExt.paddings.medium
    ) {
        item {
            ElevatedCard {
                ApplicationInfo(
                    installedAppDetails = installedAppDetails,
                    onEditClick = { /* TODO */ },
                    onRollBackClick = onRollbackClick,
                    onDeleteClick = {
                        isShowDeleteDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = MaterialThemeExt.paddings.medium,
                            horizontal = MaterialThemeExt.paddings.large
                        )
                )
            }
        }
        item {
            ElevatedCard {
                ApplicationWorkloads(
                    modifier = Modifier.padding(
                        vertical = MaterialThemeExt.paddings.medium,
                        horizontal = MaterialThemeExt.paddings.large
                    )
                )
            }
        }
        item {
            ElevatedCard {
                ApplicationHistory(
                    onRefreshClick = { /*TODO*/ },
                    modifier = Modifier.padding(
                        vertical = MaterialThemeExt.paddings.medium,
                        horizontal = MaterialThemeExt.paddings.large
                    )
                )
            }
        }
        installedAppDetails.notes?.let {
            item {
                ElevatedCard {
                    ApplicationNotes(
                        note = it,
                        modifier = Modifier.padding(
                            vertical = MaterialThemeExt.paddings.medium,
                            horizontal = MaterialThemeExt.paddings.large
                        )
                    )
                }
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
