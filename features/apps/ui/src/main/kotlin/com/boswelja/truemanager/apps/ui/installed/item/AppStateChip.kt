package com.boswelja.truemanager.apps.ui.installed.item

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.apps.logic.installed.InstalledApplication

@Composable
internal fun AppStateChip(
    state: InstalledApplication.State,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier
    ) {
        when (state) {
            InstalledApplication.State.STOPPED -> {
                Text(
                    text = "Stopped",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            InstalledApplication.State.ACTIVE -> {
                Text(
                    text = "Active",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            InstalledApplication.State.DEPLOYING -> {
                Text(
                    text = "Deploying",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppStateChipPreview(
    @PreviewParameter(provider = ApplicationStateProvider::class) state: InstalledApplication.State
) {
    MaterialTheme {
        AppStateChip(
            state = state,
            modifier = Modifier.padding(4.dp)
        )
    }
}

internal class ApplicationStateProvider : PreviewParameterProvider<InstalledApplication.State> {
    override val values: Sequence<InstalledApplication.State> =
        InstalledApplication.State.entries.asSequence()
}
