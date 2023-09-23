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
import com.nasdroid.storage.ui.pools.overview.StorageOverviewScreen

/**
 * A screen that will take control of displaying pool details on large-screen devices to provide a
 * split view. See [StorageOverviewScreen] for the overview screen.
 */
@Composable
fun StoragePoolsScreen(
    onNavigate: (route: String) -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    if (windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium) {
        var currentDetailsId by rememberSaveable {
            mutableIntStateOf(-1)
        }
        Row(modifier) {
            StorageOverviewScreen(
                onShowDetails = { currentDetailsId = it },
                modifier = Modifier.weight(1f),
                contentPadding = contentPadding
            )
            VerticalDivider()
            if (currentDetailsId >= 0) {
                // TODO Details screen
            } else {
                EmptyDetailsScreen(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(contentPadding))
            }
        }
    } else {
        StorageOverviewScreen(
            onShowDetails = {
                onNavigate("poolDetails/$it")
            },
            modifier = modifier,
            contentPadding = contentPadding
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
