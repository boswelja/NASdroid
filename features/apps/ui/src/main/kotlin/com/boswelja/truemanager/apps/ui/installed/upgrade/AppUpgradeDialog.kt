package com.boswelja.truemanager.apps.ui.installed.upgrade

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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

/**
 * A Dialog containing [UpgradeOptionsPicker].
 */
@Composable
fun AppUpgradeDialog(
    metadata: UpgradeMetadata,
    onStartUpgrade: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var targetVersion by remember(metadata) {
        mutableStateOf(metadata.availableVersions.last())
    }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            FilledTonalButton(
                onClick = { onStartUpgrade(targetVersion) },
                enabled = targetVersion.isNotEmpty()
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
                    .data(metadata.iconUrl)
                    .decoderFactory(SvgDecoder.Factory())
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
            )
        },
        title = { Text(metadata.appName) },
        text = {
            UpgradeOptionsPicker(
                targetVersion = targetVersion,
                onTargetVersionChanged = { targetVersion = it },
                upgradeMetadata = metadata
            )
        }
    )
}
