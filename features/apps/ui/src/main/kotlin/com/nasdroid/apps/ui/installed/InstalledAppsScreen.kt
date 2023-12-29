package com.nasdroid.apps.ui.installed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nasdroid.apps.ui.installed.overview.InstalledAppsOverviewScreen

/**
 * An adaptive screen that will display [InstalledAppsOverviewScreen] on all screen sizes, and [TODO]
 * on larger devices in a split view.
 */
@Composable
fun InstalledAppsScreen(
    windowSizeClass: WindowSizeClass,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    if (windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium) {
        Row(modifier) {
            InstalledAppsOverviewScreen(
                onNavigate = onNavigate,
                modifier = Modifier.fillMaxWidth(0.5f),
                contentPadding = contentPadding,
            )
            Box(modifier = Modifier.fillMaxWidth().padding(contentPadding))
        }
    } else {
        InstalledAppsOverviewScreen(
            onNavigate = onNavigate,
            modifier = modifier,
            contentPadding = contentPadding,
        )
    }
}
