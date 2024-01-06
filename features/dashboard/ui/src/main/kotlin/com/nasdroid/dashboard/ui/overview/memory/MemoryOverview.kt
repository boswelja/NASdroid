package com.nasdroid.dashboard.ui.overview.memory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.capacity.Capacity
import com.nasdroid.capacity.Capacity.Companion.gigabytes
import com.nasdroid.capacity.CapacityUnit
import com.nasdroid.dashboard.logic.dataloading.memory.MemorySpecs
import com.nasdroid.dashboard.logic.dataloading.memory.MemoryUsageData
import com.nasdroid.dashboard.ui.overview.common.OverviewItemListItem
import com.nasdroid.dashboard.ui.R
import com.nasdroid.dashboard.ui.overview.skeleton
import com.nasdroid.design.MaterialThemeExt
import org.koin.androidx.compose.koinViewModel

/**
 * A Card displaying the given system memory information.
 */
@Composable
fun MemoryOverview(
    modifier: Modifier = Modifier,
    viewModel: MemoryOverviewViewModel = koinViewModel()
) {
    val specs by viewModel.memorySpecs.collectAsState()
    val utilisation by viewModel.memoryUsageData.collectAsState()

    val error by remember(specs, utilisation) {
        derivedStateOf { specs?.exceptionOrNull() ?: utilisation?.exceptionOrNull() }
    }

    Box(
        Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        MemoryOverview(
            specs = specs?.getOrNull(),
            utilisation = utilisation?.getOrNull(),
            modifier = modifier
        )

        if (error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialThemeExt.colorScheme.errorContainer,
                        shape = MaterialThemeExt.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Something went wrong",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialThemeExt.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
internal fun MemoryOverview(
    specs: MemorySpecs?,
    utilisation: MemoryUsageData?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        OverviewItemListItem(
            labelContent = {
                if (specs?.isEcc == true) {
                    Text(stringResource(R.string.memory_total_ecc_label))
                } else {
                    Text(stringResource(R.string.memory_total_label))
                }
            },
            content = {
                Text((specs?.totalCapacity ?: 1.gigabytes).formatToString())
            },
            modifier = Modifier.skeleton(specs == null)
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MemoryUtilisationLabel(
                name = "Used",
                usage = (utilisation?.used ?: 1.gigabytes).formatToString(),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.skeleton(utilisation == null)
            )
            MemoryUtilisationLabel(
                name = stringResource(R.string.memory_usage_free),
                usage = (utilisation?.free ?: 1.gigabytes).formatToString(),
                horizontalAlignment = Alignment.End,
                modifier = Modifier.skeleton(utilisation == null)
            )
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { utilisation?.allocatedPercent ?: 0.5f },
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth()
                .clip(CircleShape)
                .skeleton(utilisation == null),
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
 * @param horizontalAlignment The alignment of the label content.
 */
@Composable
fun MemoryUtilisationLabel(
    name: String,
    usage: String,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
) {
    Column(
        modifier = Modifier
            .semantics(mergeDescendants = true) {}
            .then(modifier),
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = name,
            style = MaterialThemeExt.typography.labelMedium
        )
        Text(
            text = usage,
            style = MaterialThemeExt.typography.labelLarge
        )
    }
}

/**
 * Converts this Capcity into a human-readable size string.
 */
@Composable
fun Capacity.formatToString(): String {
    return remember(this) {
        val size = toDouble(CapacityUnit.GIBIBYTE)
        "%.1f GiB".format(size)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun MemoryCardPreview() {
    MaterialThemeExt {
        MemoryOverview(
            specs = MemorySpecs(
                isEcc = true,
                totalCapacity = 128.gigabytes
            ),
            utilisation = MemoryUsageData(
                used = 51.1.gigabytes,
                free = 128.gigabytes - 52.1.gigabytes,
                cached = 1.gigabytes,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
