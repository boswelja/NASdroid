package com.boswelja.truemanager.storage.overview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.core.api.v2.pool.Pool
import com.boswelja.truemanager.core.api.v2.pool.Topology
import com.boswelja.truemanager.core.api.v2.pool.VDev
import org.koin.androidx.compose.koinViewModel

@Composable
fun StorageOverviewScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: StorageOverviewViewModel = koinViewModel(),
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val pools by viewModel.pools.collectAsState()

    PoolsList(
        pools = pools,
        modifier = modifier,
        contentPadding = contentPadding,
    )
}

@Composable
fun PoolsList(
    pools: List<Pool>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = pools,
            key = { it.id }
        ) { pool ->
            PoolCard(
                pool = pool,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PoolCard(
    pool: Pool,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    val dividerPadding by animateDpAsState(targetValue = if (expanded) 32.dp else 0.dp, label = "Card divider padding")
    OutlinedCard(modifier) {
        Column {
            PoolOverview(
                pool = pool,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dividerPadding)
            )
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Topology(
                    topology = pool.topology,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                )
            }
            TextButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(if (expanded) "Collapse" else "Expand")
            }
        }
    }
}

@Composable
fun PoolOverview(
    pool: Pool,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = pool.name,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        StorageUseSummary(
            usedBytes = pool.capacity.allocatedBytes,
            totalBytes = pool.capacity.sizeBytes,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Topology(
    topology: Topology,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Topology",
            style = MaterialTheme.typography.titleMedium
        )
        TopologySection(
            title = { Text("Data") },
            vdevs = topology.data,
            modifier = Modifier.fillMaxWidth()
        )
        TopologySection(
            title = { Text("Cache") },
            vdevs = topology.cache,
            modifier = Modifier.fillMaxWidth()
        )
        TopologySection(
            title = { Text("De-duplication") },
            vdevs = topology.dedup,
            modifier = Modifier.fillMaxWidth()
        )
        TopologySection(
            title = { Text("Log") },
            vdevs = topology.log,
            modifier = Modifier.fillMaxWidth()
        )
        TopologySection(
            title = { Text("Spare") },
            vdevs = topology.spare,
            modifier = Modifier.fillMaxWidth()
        )
        TopologySection(
            title = { Text("Metadata") },
            vdevs = topology.special,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TopologySection(
    title: @Composable () -> Unit,
    vdevs: List<VDev>,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        ProvideTextStyle(MaterialTheme.typography.labelLarge) {
            title()
        }
        if (vdevs.isNotEmpty()) {
            vdevs.forEach { vdev ->
                if (vdev.children.isNotEmpty()) {
                    Text("• ${vdev.type} | ${vdev.children.count()} wide")
                } else {
                    Text("• ${vdev.type} | No redundancy")
                }
            }
        } else {
            Text("• VDEVs not assigned")
        }
    }
}
