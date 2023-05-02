package com.boswelja.truemanager.dashboard.ui.overview

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
import com.boswelja.truemanager.dashboard.R
import com.boswelja.truemanager.dashboard.ui.overview.common.CardListItem
import com.boswelja.truemanager.dashboard.ui.overview.common.DashboardCard
import java.text.NumberFormat

/**
 * A Card displaying the given CPU information. Relevant text in the card is selectable.
 */
@Composable
fun CpuCard(
    info: CpuInfo,
    usage: CpuUsage,
    modifier: Modifier = Modifier
) {
    DashboardCard(
        title = { Text(stringResource(R.string.cpu_card_title)) },
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CpuUsageBar(
                    usage = usage.avgUsage,
                    modifier = Modifier
                        .width(48.dp)
                        .weight(1f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(usage.avgUsage.formattedPercent())
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CardListItem(
                    labelContent = { Text(stringResource(R.string.cpu_name_label)) },
                    content = { Text(info.name) }
                )
                CardListItem(
                    labelContent = { Text(stringResource(R.string.cpu_cores_threads_label)) },
                    content = { Text(stringResource(R.string.cpu_cores_threads_count, info.cores, info.threads)) }
                )
                CardListItem(
                    labelContent = { Text(stringResource(R.string.cpu_temp_label)) },
                    content = { Text(stringResource(R.string.cpu_temperature_celsius, usage.tempCelsius)) }
                )
            }
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
    return remember(formatter) {
        formatter.format(this)
    }
}

/**
 * Describes the CPU in the system.
 *
 * @property name The name of the CPU. E.g. "Intel(R) Xeon(R) CPU E5-2680".
 * @property cores The total number of cores the CPU has.
 * @property threads The total number of threads the CPU has.
 */
data class CpuInfo(
    val name: String,
    val cores: Int,
    val threads: Int,
)

/**
 * Describes current CPU utilisation.
 *
 * @property tempCelsius The CPU temperature, in celsius. This is usually measured by the hottest
 * core.
 * @property avgUsage The average CPU utilisation. The value will always be between 0 and 1.
 */
data class CpuUsage(
    val tempCelsius: Int,
    val avgUsage: Float
)

@Preview
@Composable
fun CpuCardPreview() {
    MaterialTheme {
        CpuCard(
            info = CpuInfo(
                name = "Intel(R) Xeon(R) CPU E5-2680 v4 @ 2.40GHz",
                cores = 28,
                threads = 56
            ),
            usage = CpuUsage(
                tempCelsius = 31,
                avgUsage = 0.43f
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}