package com.nasdroid.dashboard.ui.overview.cpu

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.dashboard.logic.dataloading.DashboardData
import com.nasdroid.dashboard.ui.overview.cards.common.OverviewItemListItem
import com.nasdroid.dashboard.ui.R
import java.text.NumberFormat

/**
 * Displays the given CPU information. Relevant text in the card is selectable, and animations are
 * applied where appropriate.
 */
@Composable
fun CpuOverview(
    data: DashboardData.CpuData,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.height(IntrinsicSize.Min).then(modifier)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CpuUsageBar(
                usage = data.utilisation,
                modifier = Modifier
                    .width(48.dp)
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(data.utilisation.formattedPercent())
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OverviewItemListItem(
                labelContent = { Text(stringResource(R.string.cpu_name_label)) },
                content = { Text(data.name) }
            )
            OverviewItemListItem(
                labelContent = { Text(stringResource(R.string.cpu_cores_threads_label)) },
                content = { Text(stringResource(R.string.cpu_cores_threads_count, data.cores, data.threads)) }
            )
            OverviewItemListItem(
                labelContent = { Text(stringResource(R.string.cpu_temp_label)) },
                content = { Text(stringResource(R.string.cpu_temperature_celsius, data.tempCelsius)) }
            )
        }
    }
}

/**
 * A vertical progress bar that is used to represent CPU utilisation. Progress builds from the
 * bottom to the top.
 */
@Composable
fun CpuUsageBar(
    usage: Float,
    modifier: Modifier = Modifier
) {
    val animatedUsageFloat by animateFloatAsState(targetValue = usage, label = "CPU usage animation")
    Box(
        modifier = Modifier
            .then(modifier)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(animatedUsageFloat)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium.copy(
                        bottomStart = ZeroCornerSize,
                        bottomEnd = ZeroCornerSize
                    )
                )
        )
    }
}

@Composable
private fun Float.formattedPercent(): String {
    val formatter = remember {
        NumberFormat.getPercentInstance()
    }
    return remember(this, formatter) {
        formatter.format(this)
    }
}

@Preview(showBackground = true)
@Composable
fun CpuOverviewPreview() {
    MaterialTheme {
        CpuOverview(
            data = DashboardData.CpuData(
                name = "Intel(R) Xeon(R) CPU E5-2680 v4 @ 2.40GHz",
                cores = 28,
                threads = 56,
                tempCelsius = 31,
                utilisation = 0.43f,
                uid = 0
            ),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
    }
}
