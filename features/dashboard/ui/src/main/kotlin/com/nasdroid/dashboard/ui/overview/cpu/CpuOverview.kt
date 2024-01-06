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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.ZeroCornerSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.dashboard.logic.dataloading.cpu.CpuSpecs
import com.nasdroid.dashboard.logic.dataloading.cpu.CpuUsageData
import com.nasdroid.dashboard.ui.overview.common.OverviewItemListItem
import com.nasdroid.dashboard.ui.R
import com.nasdroid.dashboard.ui.overview.skeleton
import com.nasdroid.design.MaterialThemeExt
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat

/**
 * Displays the given CPU information. Relevant text in the card is selectable, and animations are
 * applied where appropriate.
 */
@Composable
fun CpuOverview(
    modifier: Modifier = Modifier,
    viewModel: CpuOverviewViewModel = koinViewModel(),
) {
    val specs by viewModel.cpuSpecs.collectAsState()
    val utilisation by viewModel.cpuUsageData.collectAsState()

    val error by remember(specs, utilisation) {
        derivedStateOf { specs?.exceptionOrNull() ?: utilisation?.exceptionOrNull() }
    }

    Box(
        Modifier
            .height(IntrinsicSize.Min)
            .width(IntrinsicSize.Max)
    ) {
        CpuOverview(specs = specs?.getOrNull(), utilisation = utilisation?.getOrNull(), modifier = modifier)

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
internal fun CpuOverview(
    specs: CpuSpecs?,
    utilisation: CpuUsageData?,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .then(modifier)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CpuUsageBar(
                usage = utilisation?.utilisation ?: 0f,
                modifier = Modifier
                    .width(48.dp)
                    .weight(1f)
                    .skeleton(utilisation == null)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = utilisation?.utilisation?.formattedPercent() ?: "50%",
                modifier = Modifier.skeleton(utilisation == null)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OverviewItemListItem(
                labelContent = { Text(stringResource(R.string.cpu_name_label)) },
                content = { Text(specs?.model ?: "CPU Model") },
                modifier = Modifier.skeleton(specs == null)
            )
            OverviewItemListItem(
                labelContent = { Text(stringResource(R.string.cpu_cores_threads_label)) },
                content = {
                    Text(
                        text = stringResource(
                            R.string.cpu_cores_threads_count,
                            specs?.physicalCores ?: 0,
                            specs?.totalCores ?: 0
                        )
                    )
                },
                modifier = Modifier.skeleton(specs == null)
            )
            OverviewItemListItem(
                labelContent = { Text(stringResource(R.string.cpu_temp_label)) },
                content = { Text(stringResource(R.string.cpu_temperature_celsius, utilisation?.temp ?: 0)) },
                modifier = Modifier.skeleton(specs == null)
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
            .clip(MaterialThemeExt.shapes.medium)
            .background(MaterialThemeExt.colorScheme.surfaceVariant)
            .then(modifier),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(animatedUsageFloat)
                .background(
                    color = MaterialThemeExt.colorScheme.primary,
                    shape = MaterialThemeExt.shapes.medium.copy(
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
    MaterialThemeExt {
        CpuOverview(
            specs = CpuSpecs(
                model = "Intel(R) Xeon(R) CPU E5-2680 v4 @ 2.40GHz",
                physicalCores = 28,
                totalCores = 56,
            ),
            utilisation = CpuUsageData(
                utilisation = 0.43f,
                temp = 31,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
