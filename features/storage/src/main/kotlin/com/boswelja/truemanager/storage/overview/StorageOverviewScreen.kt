package com.boswelja.truemanager.storage.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.core.api.v2.pool.Pool
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
