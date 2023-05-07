package com.boswelja.truemanager.dashboard.ui.overview

import android.text.format.Formatter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SettingsEthernet
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.dashboard.ui.overview.common.CardListItem
import com.boswelja.truemanager.dashboard.ui.overview.common.DashboardCard
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.endAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kotlinx.datetime.LocalDateTime
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NetworkCard(
    trafficData: NetworkTrafficData,
    adaptersInfo: List<AdapterInfo>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val model = remember(trafficData) {
        entryModelOf(
            entriesOf(*trafficData.outgoingBytes.toTypedArray()),
            entriesOf(*trafficData.incomingBytes.toTypedArray())
        )
    }
    DashboardCard(
        title = { Text("Network") },
        modifier = modifier,
    ) {
        val chartStyle = m3ChartStyle()
        ProvideChartStyle(chartStyle) {
            Chart(
                chart = lineChart(),
                bottomAxis = bottomAxis(label = null),
                endAxis = endAxis(
                    valueFormatter = { value, _ ->
                        Formatter.formatFileSize(context, value.toLong())
                    }
                ),
                model = model,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            color = Color(chartStyle.lineChart.lines.first().lineColor),
                            shape = MaterialTheme.shapes.extraSmall
                        )
                )
                Spacer(Modifier.width(4.dp))
                Text(text = "Outgoing", style = MaterialTheme.typography.labelLarge)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            color = Color(chartStyle.lineChart.lines[1].lineColor),
                            shape = MaterialTheme.shapes.extraSmall
                        )
                )
                Spacer(Modifier.width(4.dp))
                Text(text = "Incoming", style = MaterialTheme.typography.labelLarge)
            }
        }
        if (adaptersInfo.isNotEmpty()) {
            Divider(Modifier.padding(vertical = 8.dp))
            adaptersInfo.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SettingsEthernet, contentDescription = null)
                    Spacer(Modifier.width(16.dp))
                    Text("${it.name} \u2022 ${it.address}")
                }
            }
        }
    }
}

/**
 * Describes incoming and outgoing network traffic data over a period of time.
 *
 * @property incomingBytes A list of incoming bytes, where index 0 occurred at the start of [period]
 * and last index occurred at the end of [period].
 * @property outgoingBytes A list of outgoing bytes, where index 0 occurred at the start of [period]
 * and last index occurred at the end of [period].
 * @property period A range of [LocalDateTime]s that the data occurred over.
 */
data class NetworkTrafficData(
    val incomingBytes: List<Long>,
    val outgoingBytes: List<Long>,
    val period: ClosedRange<LocalDateTime>
)

data class AdapterInfo(
    val name: String,
    val address: String
)

@Preview
@Composable
fun NetworkCardPreview() {
    val data = remember {
        val incomingRand = Random(0)
        val outgoingRandom = Random(10)
        NetworkTrafficData(
            incomingBytes = List(100) { incomingRand.nextLong(0, 5000) },
            outgoingBytes = List(100) { outgoingRandom.nextLong(0, 5000) },
            period = LocalDateTime(
                year = 2023,
                monthNumber = 5,
                dayOfMonth = 5,
                hour = 21,
                minute = 0
            )..LocalDateTime(
                year = 2023,
                monthNumber = 5,
                dayOfMonth = 5,
                hour = 22,
                minute = 0
            )
        )
    }
    NetworkCard(
        trafficData = data,
        adaptersInfo = listOf(
            AdapterInfo(
                name = "eno1",
                address = "192.168.1.2"
            ),
            AdapterInfo(
                name = "eno2",
                address = "192.168.1.3"
            )
        )
    )
}
