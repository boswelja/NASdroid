package com.nasdroid.apps.ui.installed.overview.item

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.nasdroid.apps.logic.installed.InstalledAppOverview
import com.nasdroid.apps.ui.R
import com.nasdroid.design.MaterialThemeExt

@Composable
internal fun AppStateChip(
    state: InstalledAppOverview.State,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialThemeExt.shapes.small,
        color = when (state) {
            InstalledAppOverview.State.DEPLOYING -> MaterialThemeExt.colorScheme.primaryContainer
            InstalledAppOverview.State.ACTIVE -> MaterialThemeExt.colorScheme.secondaryContainer
            InstalledAppOverview.State.STOPPED -> MaterialThemeExt.colorScheme.tertiaryContainer
        },
        modifier = modifier
    ) {
        when (state) {
            InstalledAppOverview.State.STOPPED -> {
                Text(
                    text = stringResource(R.string.app_state_stopped),
                    style = MaterialThemeExt.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            InstalledAppOverview.State.ACTIVE -> {
                Text(
                    text = stringResource(R.string.app_state_running),
                    style = MaterialThemeExt.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            InstalledAppOverview.State.DEPLOYING -> {
                Text(
                    text = stringResource(R.string.app_state_deploying),
                    style = MaterialThemeExt.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppStateChipPreview(
    @PreviewParameter(provider = ApplicationStateProvider::class) state: InstalledAppOverview.State
) {
    MaterialTheme {
        AppStateChip(
            state = state,
            modifier = Modifier.padding(4.dp)
        )
    }
}

internal class ApplicationStateProvider : PreviewParameterProvider<InstalledAppOverview.State> {
    override val values: Sequence<InstalledAppOverview.State> =
        InstalledAppOverview.State.entries.asSequence()
}
