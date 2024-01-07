package com.nasdroid.apps.ui.installed.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.nasdroid.apps.logic.installed.InstalledAppDetails
import com.nasdroid.apps.ui.R
import com.nasdroid.core.urllauncher.rememberUrlLauncher
import com.nasdroid.design.MaterialThemeExt

/**
 * Displays information to the user about the provided [InstalledAppDetails].
 */
@Composable
fun ApplicationInfo(
    installedAppDetails: InstalledAppDetails,
    onEditClick: () -> Unit,
    onRollBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isShowSourcesModal by rememberSaveable {
        mutableStateOf(false)
    }
    Column(modifier) {
        val itemModifier = Modifier.padding(vertical = 8.dp)
        AppIcon(
            iconUrl = installedAppDetails.iconUrl,
            modifier = Modifier
                .size(128.dp)
                .align(Alignment.CenterHorizontally)
                .clip(MaterialThemeExt.shapes.medium)
        )
        ApplicationInfoItem(
            label = stringResource(R.string.app_info_name),
            text = installedAppDetails.name,
            modifier = itemModifier
        )
        ApplicationInfoItem(
            label = stringResource(R.string.app_info_app_version),
            text = installedAppDetails.appVersion,
            modifier = itemModifier
        )
        ApplicationInfoItem(
            label = stringResource(R.string.app_info_chart_version),
            text = installedAppDetails.chartVersion,
            modifier = itemModifier
        )
        TextButton(
            onClick = { isShowSourcesModal = true },
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(stringResource(R.string.app_info_view_sources))
        }
        SourceCatalogDetails(
            catalog = installedAppDetails.catalog,
            train = installedAppDetails.train,
            modifier = itemModifier
        )
        ApplicationControls(
            onEditClick = onEditClick,
            onRollBackClick = onRollBackClick,
            onDeleteClick = onDeleteClick,
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp)
        )
    }

    if (isShowSourcesModal) {
        val urlLauncher = rememberUrlLauncher()
        SourcesListModal(
            sources = installedAppDetails.sources,
            onSourceClick = { urlLauncher.launchUrl(it) },
            onDismiss = { isShowSourcesModal = false }
        )
    }
}

@Composable
internal fun AppIcon(
    iconUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(iconUrl)
            .decoderFactory(SvgDecoder.Factory())
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
internal fun SourceCatalogDetails(
    catalog: String,
    train: String,
    modifier: Modifier = Modifier
) {
    ApplicationInfoItem(
        label = stringResource(R.string.app_info_catalog_train),
        text ="$catalog \u2022 $train",
        modifier = modifier
    )
}

@Composable
internal fun ApplicationInfoItem(
    label: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = label,
            style = MaterialThemeExt.typography.labelMedium,
            color = MaterialThemeExt.colorScheme.onSurfaceVariant
        )
        Text(
            text = text,
            style = MaterialThemeExt.typography.bodyLarge,
            color = MaterialThemeExt.colorScheme.onSurface
        )
    }
}

@Composable
internal fun ApplicationControls(
    onEditClick: () -> Unit,
    onRollBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedIconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, stringResource(R.string.app_control_edit))
        }
        OutlinedIconButton(onClick = onRollBackClick) {
            Icon(Icons.Default.Restore, stringResource(R.string.app_control_rollback))
        }
        OutlinedIconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, stringResource(R.string.app_control_delete))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SourcesListModal(
    sources: List<String>,
    onSourceClick: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        if (sources.isNotEmpty()) {
            Text(
                text = stringResource(R.string.app_info_sources_header),
                style = MaterialThemeExt.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            sources.forEach { url ->
                ListItem(
                    overlineContent = {
                        Text(
                            text = when {
                                url.contains("github.com") -> stringResource(R.string.app_info_source_github)
                                url.contains("hub.docker.com") -> stringResource(R.string.app_info_source_docker)
                                else -> stringResource(R.string.app_info_source_unknown)
                            }
                        )
                    },
                    headlineContent = { Text(url) },
                    modifier = Modifier.clickable { onSourceClick(url) }
                )
            }
        } else {
            Text(
                text = stringResource(R.string.app_info_source_missing),
                style = MaterialThemeExt.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ApplicationInfoPreview() {
    MaterialThemeExt {
        ApplicationInfo(
            installedAppDetails = InstalledAppDetails(
                name = "adguard-home",
                iconUrl = "https://media.sys.truenas.net/apps/adguard-home/icons/icon.svg",
                appVersion = "0.107.43",
                chartVersion = "1.0.32",
                sources = listOf(
                    "https://github.com/AdguardTeam/AdGuardHome",
                    "https://github.com/truenas/charts/tree/master/community/adguard-home",
                    "https://hub.docker.com/r/adguard/adguardhome"
                ),
                catalog = "TrueNAS",
                train = "community",
                developer = null,
                lastUpdated = null,
                state = InstalledAppDetails.State.ACTIVE,
                notes = "",
                hasUpdateAvailable = false,
                webPortalUrl = "https://google.com/"
            ),
            onEditClick = { },
            onRollBackClick = { },
            onDeleteClick = { },
            modifier = Modifier.padding(16.dp)
        )
    }
}
