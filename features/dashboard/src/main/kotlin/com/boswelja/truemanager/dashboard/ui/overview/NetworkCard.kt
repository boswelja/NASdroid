package com.boswelja.truemanager.dashboard.ui.overview

import android.text.format.Formatter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.dashboard.R
import com.boswelja.truemanager.dashboard.ui.overview.common.DashboardCard
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.endAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import kotlinx.datetime.LocalDateTime
import kotlin.random.Random

/**
 * A Card displaying the given system network information.
 *
 * @param trafficData The incoming and outgoing traffic data for all adapters combined.
 * @param adaptersInfo Details about network adapters.
 * @param modifier [Modifier].
 */
@Composable
fun NetworkCard(
    trafficData: NetworkTrafficData,
    adaptersInfo: List<AdapterInfo>,
    modifier: Modifier = Modifier
) {
    DashboardCard(
        title = { Text(stringResource(R.string.network_card_title)) },
        modifier = modifier,
    ) {
        TrafficDataChart(trafficData = trafficData)
        if (adaptersInfo.isNotEmpty()) {
            Divider(Modifier.padding(vertical = 8.dp))
            adaptersInfo.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SettingsEthernet, contentDescription = null)
                    Spacer(Modifier.width(16.dp))
                    Text(stringResource(R.string.network_adapter_simple, it.name, it.address))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TrafficDataChart(
    trafficData: NetworkTrafficData,
    modifier: Modifier = Modifier,
    chartStyle: ChartStyle = m3ChartStyle()
) {
    val context = LocalContext.current

    val model = remember(trafficData) {
        entryModelOf(
            trafficData.outgoingBytes.mapIndexed { index, y -> entryOf(index, y) },
            trafficData.incomingBytes.mapIndexed { index, y -> entryOf(index, y) }
        )
    }

    Column(modifier) {
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
        Spacer(Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ChartLegendItem(
                label = stringResource(R.string.network_outgoing_label),
                color = Color(chartStyle.lineChart.lines.first().lineColor)
            )
            ChartLegendItem(
                label = stringResource(R.string.network_incoming_label),
                color = Color(chartStyle.lineChart.lines[1].lineColor)
            )
        }
    }
}

@Composable
internal fun ChartLegendItem(
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color = color, shape = MaterialTheme.shapes.extraSmall)
        )
        Spacer(Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelLarge)
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

/**
 * Describes the basics of a network adapter.
 *
 * @property name The name of the adapter.
 * @property address The IP address of the adapter on the network.
 */
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
