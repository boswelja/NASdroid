package com.boswelja.truemanager.dashboard.ui.overview.cards

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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boswelja.capacity.Capacity.Companion.bytes
import com.boswelja.capacity.CapacityUnit
import com.boswelja.truemanager.dashboard.logic.DashboardData
import com.boswelja.truemanager.dashboard.ui.R
import com.boswelja.truemanager.dashboard.ui.overview.cards.common.OverviewItemListItem
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
 * A Card displaying an overview of the system network status..
 *
 * @param data The incoming and outgoing traffic data for all adapters.
 * @param modifier [Modifier].
 */
@Composable
fun NetworkOverview(
    data: DashboardData.NetworkUsageData,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        data.adaptersData.forEachIndexed { index, adapterData ->
            if (index > 0) Divider(Modifier.padding(vertical = 8.dp))
            AdapterInfo(adapterData = adapterData)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun AdapterInfo(
    adapterData: DashboardData.NetworkUsageData.AdapterData,
    modifier: Modifier = Modifier,
    chartStyle: ChartStyle = m3ChartStyle()
) {
    val model = remember(adapterData) {
        entryModelOf(
            adapterData.sentBytes.mapIndexed { index, y -> entryOf(index, y) },
            adapterData.receivedBytes.mapIndexed { index, y -> entryOf(index, y) }
        )
    }

    Column(modifier) {
        Text(
            text = adapterData.name,
            style = MaterialTheme.typography.titleMedium,
        )
        ProvideChartStyle(chartStyle) {
            Chart(
                chart = lineChart(),
                bottomAxis = bottomAxis(label = null),
                endAxis = endAxis(
                    valueFormatter = { value, _ ->
                        val mb = value.toLong().bytes.toDouble(CapacityUnit.KILOBYTE)
                        "%.1f KB".format(mb)
                    }
                ),
                model = model,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
            )
        }
        Spacer(Modifier.height(4.dp))
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
        Spacer(Modifier.height(8.dp))
        OverviewItemListItem(labelContent = { Text("Address") }) {
            Text(adapterData.address)
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

@Preview
@Composable
fun NetworkOverviewPreview() {
    val data = remember {
        val incomingRand = Random(0)
        val outgoingRandom = Random(10)
        DashboardData.NetworkUsageData(
            uid = "network",
            adaptersData = listOf(
                DashboardData.NetworkUsageData.AdapterData(
                    name = "eno1",
                    address = "192.168.1.2",
                    receivedBytes = List(100) { incomingRand.nextDouble(0.0, 5000.0) },
                    sentBytes = List(100) { outgoingRandom.nextDouble(0.0, 5000.0) },
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
                ),
                DashboardData.NetworkUsageData.AdapterData(
                    name = "eno2",
                    address = "192.168.1.3",
                    receivedBytes = List(100) { incomingRand.nextDouble(0.0, 5000.0) },
                    sentBytes = List(100) { outgoingRandom.nextDouble(0.0, 5000.0) },
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
            )
        )
    }
    NetworkOverview(data = data)
}
