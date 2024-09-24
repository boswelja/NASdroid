package com.nasdroid.storage.ui.pools.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nasdroid.navigation.BackNavigationScaffold

@Composable
fun PoolDetailsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackNavigationScaffold(
        title = {
            Text("Pool")
        },
        onNavigateBack = onNavigateBack,
        modifier = modifier
    ) {

    }
}
