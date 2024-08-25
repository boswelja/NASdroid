package com.nasdroid.storage.ui.pools.overview

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
import com.nasdroid.storage.logic.pool.PoolOverview
import org.koin.androidx.compose.koinViewModel

/**
 * Displays an overview of all storage pools on the server.
 */
@Composable
fun PoolsScreen(
    onShowDetails: (poolId: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: PoolsViewModel = koinViewModel(),
) {
    // TODO handle loading
    val isLoading by viewModel.isLoading.collectAsState()
    val pools by viewModel.pools.collectAsState()

    PoolsList(
        onShowDetails = onShowDetails,
        pools = pools,
        modifier = modifier,
        contentPadding = contentPadding,
    )
}

/**
 * Displays a list of [PoolOverview]s.
 */
@Composable
fun PoolsList(
    onShowDetails: (poolId: Int) -> Unit,
    pools: List<PoolOverview>,
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
                onShowDetails = { onShowDetails(pool.id) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
