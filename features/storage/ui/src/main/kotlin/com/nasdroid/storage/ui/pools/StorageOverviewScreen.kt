package com.nasdroid.storage.ui.pools

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
) {
    NavigationSuiteScaffold(
        title = { Text(stringResource(R.string.storage_dashboard_title)) },
        onNavigate = onNavigate,
        modifier = modifier
    ) { contentPadding ->
        PoolsScreen(
            onShowDetails = {
                onNavigate("poolDetails/$it")
            },
            contentPadding = contentPadding + PaddingValues(
                horizontal = MaterialThemeExt.paddings.large,
                vertical = MaterialThemeExt.paddings.medium
            )
        )
    }
}
