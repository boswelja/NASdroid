package com.boswelja.truemanager.dashboard.ui.overview

import android.os.Build
import android.text.format.Formatter
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.dashboard.R
import com.boswelja.truemanager.dashboard.ui.overview.common.CardListItem
import com.boswelja.truemanager.dashboard.ui.overview.common.DashboardCard
import com.boswelja.truemanager.dashboard.ui.overview.common.LinearMultiProgressIndicator

/**
 * A Card displaying the given system memory information.
 */
@Composable
fun MemoryCard(
    memoryInfo: MemoryInfo,
    memoryUsage: MemoryUsage,
    modifier: Modifier = Modifier
) {
    DashboardCard(
        title = { Text(stringResource(R.string.memory_card_title)) },
        modifier = modifier
    ) {
        CardListItem(
            labelContent = {
                if (memoryInfo.isEcc) {
                    Text(stringResource(R.string.memory_total_ecc_label))
                } else {
                    Text(stringResource(R.string.memory_total_label))
                }
            }
        ) {
            Text(fileSizeString(bytes = memoryInfo.totalCapacityBytes))
        }
        MemoryUsageSummary(
            usage = memoryUsage,
            totalBytes = memoryInfo.totalCapacityBytes
        )
    }
}

/**
 * Displays a labelled progress bar communicating an overview of memory utilisation.
 *
 * @param usage [MemoryUsage].
 * @param totalBytes The total available bytes of memory.
 * @param modifier [Modifier].
 */
@Composable
fun MemoryUsageSummary(
    usage: MemoryUsage,
    totalBytes: Long,
    modifier: Modifier = Modifier,
) {
    val zfsCacheUsage by animateFloatAsState(
        targetValue = usage.zfsCacheBytes / totalBytes.toFloat(),
        label = "ZFS Cache memory usage"
    )
    val servicesUsage by animateFloatAsState(
        targetValue = usage.servicesBytes / totalBytes.toFloat(),
        label = "Services memory usage"
    )
    val freeUsage by remember {
        derivedStateOf {
            1 - (zfsCacheUsage + servicesUsage)
        }
    }
    val freeSpace by remember {
        derivedStateOf {
            totalBytes - (usage.servicesBytes + usage.zfsCacheBytes)
        }
    }
    Column(modifier) {
        Row(Modifier.fillMaxWidth()) {
            Spacer(Modifier.weight(servicesUsage))
            MemoryUtilisationLabel(
                name = stringResource(R.string.memory_usage_services),
                usage = fileSizeString(usage.servicesBytes)
            )
            Spacer(Modifier.weight(zfsCacheUsage))
            MemoryUtilisationLabel(
                name = stringResource(R.string.memory_usage_zfs_cache),
                usage = fileSizeString(usage.zfsCacheBytes)
            )
            Spacer(Modifier.weight(freeUsage))
            MemoryUtilisationLabel(
                name = stringResource(R.string.memory_usage_free),
                usage = fileSizeString(freeSpace)
            )
        }
        Spacer(Modifier.height(4.dp))
        LinearMultiProgressIndicator(
            progresses = listOf(servicesUsage, zfsCacheUsage)
        )
    }
}

/**
 * A text label for individual memory-utilising components. For example, this might be used to show
 * "64 GB free space".
 *
 * @param name The name of the component utilising memory. For example, "Free"
 * @param usage The human-readable usage. For example, "64 GB"
 * @param modifier [Modifier].
 */
@Composable
fun MemoryUtilisationLabel(
    name: String,
    usage: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .semantics(mergeDescendants = true) {}
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = usage,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * Describes static information about the system memory configuration.
 *
 * @property totalCapacityBytes The total memory capacity, in bytes.
 * @property isEcc Whether the memory supports ECC.
 */
data class MemoryInfo(
    val totalCapacityBytes: Long,
    val isEcc: Boolean
)

/**
 * Describes memory utilisation for the system.
 *
 * @property zfsCacheBytes The amount of memory used by the ZFS cache, in bytes.
 * @property servicesBytes The amount of memory used by various apps and services, in bytes.
 */
data class MemoryUsage(
    val zfsCacheBytes: Long,
    val servicesBytes: Long
)

/**
 * Converts the given bytes into a human-readable size string.
 */
@Composable
fun fileSizeString(bytes: Long): String {
    val context = LocalContext.current
    return remember {
        Formatter.formatFileSize(context, bytes)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun MemoryCardPreview() {
    MaterialTheme(colorScheme = dynamicLightColorScheme(LocalContext.current)) {
        MemoryCard(
            memoryInfo = MemoryInfo(
                totalCapacityBytes = 128000000000,
                isEcc = true,
            ),
            memoryUsage = MemoryUsage(
                zfsCacheBytes = 64500000000,
                servicesBytes = 19400000000,
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
