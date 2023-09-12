package com.nasdroid.dashboard.ui.overview.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.capacity.Capacity.Companion.bytes
import com.nasdroid.capacity.CapacityUnit
import com.nasdroid.dashboard.logic.dataloading.DashboardData
import com.nasdroid.dashboard.ui.overview.cards.common.OverviewItemListItem
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
            if (index > 0) HorizontalDivider(Modifier.padding(vertical = 8.dp))
            AdapterInfo(adapterData = adapterData)
        }
    }
}

@Composable
internal fun AdapterInfo(
    adapterData: DashboardData.NetworkUsageData.AdapterData,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = adapterData.name,
            style = MaterialTheme.typography.titleMedium,
        )
        OverviewItemListItem(
            labelContent = { Text("Upload") }
        ) {
            Text("%.2fMB".format(adapterData.sentBytes.last().toLong().bytes.toDouble(CapacityUnit.MEBIBYTE)))
        }
        OverviewItemListItem(
            labelContent = { Text("Download") }
        ) {
            Text("%.2fMB".format(adapterData.receivedBytes.last().toLong().bytes.toDouble(CapacityUnit.MEBIBYTE)))
        }
        OverviewItemListItem(labelContent = { Text("Address") }) {
            Text(adapterData.address)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NetworkOverviewPreview() {
    val data = remember {
        val incomingRand = Random(0)
        val outgoingRandom = Random(10)
        DashboardData.NetworkUsageData(
            uid = 0,
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
