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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CpuCard(
    info: CpuInfo,
    usage: CpuUsage,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "CPU",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CpuUsageBar(
                        usage = usage.avgUsage,
                        modifier = Modifier.width(48.dp).weight(1f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${usage.avgUsage}%")
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CpuItem(
                        labelContent = { Text("Name") },
                        content = { Text(info.name) }
                    )
                    CpuItem(
                        labelContent = { Text("Core count") },
                        content = { Text("${info.cores} cores, ${info.threads} threads") }
                    )
                    CpuItem(
                        labelContent = { Text("Temperature") },
                        content = { Text("${usage.temp}\u00B0C") }
                    )
                }
            }
        }
    }
}

@Composable
fun CpuUsageBar(
    usage: Int,
    modifier: Modifier = Modifier
) {
    val usageFloat by remember {
        derivedStateOf {
            usage / 100f
        }
    }
    val animatedUsageFloat by animateFloatAsState(targetValue = usageFloat, label = "CPU usage animation")
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

/**
 * An item in the System Information card. This simply displays some labelled content, usually text.
 */
@Composable
fun CpuItem(
    labelContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.labelLarge,
            content = labelContent
        )
        SelectionContainer {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyLarge,
                content = content
            )
        }
    }
}

data class CpuInfo(
    val name: String,
    val cores: Int,
    val threads: Int,
)

data class CpuUsage(
    val temp: Int,
    val avgUsage: Int
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
                temp = 31,
                avgUsage = 5
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}