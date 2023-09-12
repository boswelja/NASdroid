package com.nasdroid.dashboard.ui.overview.cards

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.nasdroid.dashboard.logic.dataloading.DashboardData
import com.nasdroid.dashboard.ui.overview.cards.common.OverviewItemListItem
import com.nasdroid.dashboard.ui.R

/**
 * A Card displaying the given system memory information.
 */
@Composable
fun MemoryOverview(
    data: DashboardData.MemoryData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        OverviewItemListItem(
            labelContent = {
                if (data.isEcc) {
                    Text(stringResource(R.string.memory_total_ecc_label))
                } else {
                    Text(stringResource(R.string.memory_total_label))
                }
            }
        ) {
            Text((data.memoryUsed + data.memoryFree).formatToString())
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MemoryUtilisationLabel(
                name = "Used",
                usage = data.memoryUsed.formatToString(),
                horizontalAlignment = Alignment.Start,
            )
            MemoryUtilisationLabel(
                name = stringResource(R.string.memory_usage_free),
                usage = data.memoryFree.formatToString(),
                horizontalAlignment = Alignment.End,
            )
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = data.usedPercent,
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth()
                .clip(CircleShape)
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
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = usage,
            style = MaterialTheme.typography.labelLarge
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
    MaterialTheme {
        MemoryOverview(
            data = DashboardData.MemoryData(
                memoryUsed = 51.1.gigabytes,
                memoryFree = 128.gigabytes - 51.1.gigabytes,
                isEcc = true,
                uid = 0
            ),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
    }
}
