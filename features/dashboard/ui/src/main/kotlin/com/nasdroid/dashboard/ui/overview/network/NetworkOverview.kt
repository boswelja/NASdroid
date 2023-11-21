package com.nasdroid.dashboard.ui.overview.network

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.capacity.Capacity.Companion.bytes
import com.nasdroid.capacity.CapacityUnit
import com.nasdroid.dashboard.logic.dataloading.network.NetworkConfiguration
import com.nasdroid.dashboard.logic.dataloading.network.NetworkUsageData
import com.nasdroid.dashboard.ui.overview.common.OverviewItemListItem
import com.nasdroid.dashboard.ui.overview.skeleton
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import org.koin.androidx.compose.koinViewModel

/**
 * A Card displaying an overview of the system network status.
 */
@Composable
fun NetworkOverview(
    modifier: Modifier = Modifier,
    viewModel: NetworkOverviewViewModel = koinViewModel()
) {
    val configuration by viewModel.networkConfiguration.collectAsState()
    val utilisation by viewModel.networkUsageData.collectAsState()

    val error by remember(configuration, utilisation) {
        derivedStateOf { configuration?.exceptionOrNull() ?: utilisation?.exceptionOrNull() }
    }

    Box(
        Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        NetworkOverview(
            configuration = configuration?.getOrNull(),
            utilisation = utilisation?.getOrNull(),
            modifier = modifier
        )

        if (error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Something went wrong",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
internal fun NetworkOverview(
    configuration: NetworkConfiguration?,
    utilisation: NetworkUsageData?,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        configuration?.adapters?.forEachIndexed { index, adapterData ->
            val adapterUtilisation = remember(adapterData, utilisation) {
                utilisation?.adapterUtilisation?.firstOrNull { it.name == adapterData.name }
            }
            if (index > 0) {
                HorizontalDivider(
                    Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                )
            }
            AdapterInfo(
                adapterConfig = adapterData,
                adapterUtilisation = adapterUtilisation,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
internal fun AdapterInfo(
    adapterConfig: NetworkConfiguration.NetworkAdapter?,
    adapterUtilisation: NetworkUsageData.AdapterUtilisation?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = adapterConfig?.name ?: "adapter",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.skeleton(adapterConfig == null)
        )
        OverviewItemListItem(
            labelContent = { Text("Address") },
            content = {
                Text(adapterConfig?.address ?: "None")
            },
            modifier = Modifier.skeleton(adapterConfig == null)
        )
        adapterUtilisation?.let {
            val chartModel = remember {
                entryModelOf(
                    adapterUtilisation.sentBits.bytes.toDouble(CapacityUnit.MEGABYTE),
                    adapterUtilisation.receivedBits.bytes.toDouble(CapacityUnit.MEGABYTE)
                )
            }
            ProvideChartStyle(m3ChartStyle()) {
                Chart(
                    chart = columnChart(mergeMode = ColumnChart.MergeMode.Grouped),
                    model = chartModel,
                    startAxis = rememberStartAxis(
                        title = "Mbit/s",
                        titleComponent = textComponent(color = LocalContentColor.current)
                    ),
                    bottomAxis = rememberBottomAxis(
                        valueFormatter = { value, _ ->
                            if (value == 0f) {
                                "Sent"
                            } else {
                                "Received"
                            }
                        }
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NetworkOverviewPreview() {
    NetworkOverview(
        configuration = NetworkConfiguration(
            adapters = listOf(
                NetworkConfiguration.NetworkAdapter(
                    name = "adapter1",
                    address = "192.168.1.2"
                ),
                NetworkConfiguration.NetworkAdapter(
                    name = "adapter2",
                    address = "192.168.1.3"
                )
            )
        ),
        utilisation = NetworkUsageData(
            adapterUtilisation = listOf(
                NetworkUsageData.AdapterUtilisation(
                    name = "adapter1",
                    sentBits = 99999999,
                    receivedBits = 10000000
                ),
                NetworkUsageData.AdapterUtilisation(
                    name = "adapter2",
                    sentBits = 10000000,
                    receivedBits = 99999999
                )
            )
        ),
        modifier = Modifier.padding(16.dp)
    )
}
