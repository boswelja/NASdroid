package com.nasdroid.storage.ui.pools

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.design.plus
import com.nasdroid.navigation.NavigationSuiteScaffold
import com.nasdroid.storage.ui.R
import com.nasdroid.storage.ui.pools.overview.PoolsScreen

/**
 * A screen that will take control of displaying pool details on large-screen devices to provide a
 * split view. See [PoolsScreen] for the overview screen.
 */
@Composable
fun StorageOverviewScreen(
    onNavigate: (route: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    NavigationSuiteScaffold(
        title = { Text(stringResource(R.string.storage_dashboard_title)) },
        onNavigate = onNavigate,
        modifier = modifier.padding(contentPadding)
    ) {
        PoolsScreen(
            onShowDetails = {
                onNavigate("poolDetails/$it")
            },
            contentPadding = it + PaddingValues(
                horizontal = MaterialThemeExt.paddings.large,
                vertical = MaterialThemeExt.paddings.medium
            )
        )
    }
}

@Composable
internal fun EmptyDetailsScreen(modifier: Modifier = Modifier) {
    Box(modifier) {
        Text(
            text = "Pool details will appear here",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
