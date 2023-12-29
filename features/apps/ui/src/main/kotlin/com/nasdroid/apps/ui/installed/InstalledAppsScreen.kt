package com.nasdroid.apps.ui.installed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nasdroid.apps.ui.R
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
                modifier = Modifier
                    .weight(1f)
                    .widthIn(max = 380.dp)
                    .fillMaxHeight(),
                contentPadding = contentPadding,
            )
            VerticalDivider()
            SelectAppHint(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(contentPadding)
            )
        }
    } else {
        InstalledAppsOverviewScreen(
            onNavigate = onNavigate,
            modifier = modifier,
            contentPadding = contentPadding,
        )
    }
}

@Composable
internal fun SelectAppHint(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.TwoTone.Image, contentDescription = null, modifier = Modifier.size(120.dp))
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.overview_details_pane_empty),
            textAlign = TextAlign.Center
        )
    }
}