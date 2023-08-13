package com.boswelja.truemanager.apps.ui.installed.upgrade

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
//    var targetVersion by remember(metadata) {
//        mutableStateOf(metadata.availableVersions.last())
//    }
//    AlertDialog(
//        onDismissRequest = onDismissRequest,
//        confirmButton = {
//            FilledTonalButton(
//                onClick = { onStartUpgrade(targetVersion) },
//                enabled = targetVersion.isNotEmpty()
//            ) {
//                Text("Upgrade")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismissRequest) {
//                Text("Cancel")
//            }
//        },
//        icon = {
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(metadata.iconUrl)
//                    .decoderFactory(SvgDecoder.Factory())
//                    .crossfade(true)
//                    .build(),
//                contentDescription = null,
//                modifier = Modifier
//                    .height(36.dp)
//            )
//        },
//        title = { Text(metadata.appName) },
//        text = {
//            UpgradeOptionsPicker(
//                targetVersion = targetVersion,
//                onTargetVersionChanged = { targetVersion = it },
//                upgradeMetadata = metadata
//            )
//        }
//    )
}
