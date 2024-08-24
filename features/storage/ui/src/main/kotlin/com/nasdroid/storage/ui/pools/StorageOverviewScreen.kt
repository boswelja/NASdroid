package com.nasdroid.storage.ui.pools

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    NavigationSuiteScaffold(
        title = { Text(stringResource(R.string.storage_dashboard_title)) },
        onNavigate = onNavigate,
        modifier = modifier.padding(contentPadding)
    ) {
        if (windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium) {
            var currentDetailsId by rememberSaveable {
                mutableIntStateOf(-1)
            }
            Row {
                PoolsScreen(
                    onShowDetails = { currentDetailsId = it },
                    modifier = Modifier.weight(1f),
                    contentPadding = it + PaddingValues(
                        horizontal = MaterialThemeExt.paddings.large,
                        vertical = MaterialThemeExt.paddings.medium
                    )
                )
                VerticalDivider()
                if (currentDetailsId >= 0) {
                    // TODO Details screen
                } else {
                    EmptyDetailsScreen(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(it)
                            .padding(
                                horizontal = MaterialThemeExt.paddings.large,
                                vertical = MaterialThemeExt.paddings.medium
                            )
                    )
                }
            }
        } else {
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
