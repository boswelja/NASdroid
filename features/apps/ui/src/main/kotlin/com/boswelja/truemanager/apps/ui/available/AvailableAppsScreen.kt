package com.boswelja.truemanager.apps.ui.available

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.getViewModel

/**
 * A screen for displaying and allowing installation of apps available from catalogs on the system.
 */
@Composable
fun AvailableAppsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: AvailableAppsViewModel = getViewModel()
) {
    // TODO
}
