package com.boswelja.truemanager.apps.ui.installed.upgrade

import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import org.koin.androidx.compose.getViewModel

/**
 * A Dialog containing [UpgradeOptionsPicker].
 */
@Composable
fun AppUpgradeDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppUpgradeViewModel = getViewModel(),
) {
    val upgradeDetails by viewModel.upgradeDetails.collectAsState()
    val appOverview by viewModel.appOverview.collectAsState()
    val isLoading by viewModel.loading.collectAsState()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            FilledTonalButton(
                onClick = {  },
                enabled = upgradeDetails != null
            ) {
                Text("Upgrade")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        icon = {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(appOverview?.iconUrl)
                    .decoderFactory(SvgDecoder.Factory())
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .height(36.dp)
            )
        },
        title = { Text(appOverview?.title.orEmpty()) },
        text = {
            upgradeDetails?.let { details ->
                var targetVersion by remember(details) {
                    mutableStateOf(details.targetVersion)
                }
                UpgradeOptionsPicker(
                    targetVersion = targetVersion,
                    onTargetVersionChanged = {
                        targetVersion = it
                        viewModel.setTargetVersion(it)
                    },
                    imagesToBeUpdated = details.containerImagesToUpdate,
                    availableVersions = details.availableVersions,
                    changelog = details.changelog,
                    enabled = !isLoading,
                )
            } ?: CircularProgressIndicator()

        },
        modifier = modifier,
    )
}
