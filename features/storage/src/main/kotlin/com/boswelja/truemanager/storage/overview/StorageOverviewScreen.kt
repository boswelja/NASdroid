package com.boswelja.truemanager.storage.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.core.api.v2.pool.Pool
import com.boswelja.truemanager.core.api.v2.pool.Topology
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
    OutlinedCard(modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = pool.name,
                style = MaterialTheme.typography.titleLarge,
            )
            StorageUseSummary(
                usedBytes = pool.capacity.allocatedBytes,
                totalBytes = pool.capacity.sizeBytes,
                modifier = Modifier.fillMaxWidth()
            )
            Topology(topology = pool.topology)
        }
    }
}

@Composable
fun Topology(
    topology: Topology,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        if (topology.data.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Data VDEVs")
                Text("${topology.data.count()} ${topology.data.first().type}")
            }
        }
    }
}
