package com.boswelja.truemanager.apps.ui.installed.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.boswelja.truemanager.apps.logic.installed.ApplicationOverview

/**
 * Displays information contained in the given [ApplicationOverview] as a short, reusable list item.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationOverviewItem(
    applicationOverview: ApplicationOverview,
    onClick: () -> Unit,
    onActionClick: (AppAction) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier, onClick = onClick) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(applicationOverview.iconUrl)
                        .decoderFactory(SvgDecoder.Factory())
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterVertically)
                )
                AppInfoText(
                    applicationOverview = applicationOverview,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (applicationOverview.webPortalUrl != null) {
                    FilledTonalButton(onClick = { onActionClick(AppAction.WEB_PORTAL) }) {
                        Text("Web Portal")
                    }
                }
                AppStateControlButton(
                    state = applicationOverview.state,
                    onStart = { onActionClick(AppAction.START) },
                    onStop = { onActionClick(AppAction.STOP) }
                )
                Spacer(Modifier.weight(1f))
                AppControlsOverflowMenu(
                    app = applicationOverview,
                    onControlClick = { control ->
                        when (control) {
                            AppControl.UPGRADE -> onActionClick(AppAction.UPGRADE)
                            AppControl.ROLL_BACK -> onActionClick(AppAction.ROLLBACK)
                            AppControl.EDIT -> onActionClick(AppAction.EDIT)
                            AppControl.SHELL -> onActionClick(AppAction.SHELL)
                            AppControl.LOGS -> onActionClick(AppAction.LOGS)
                            AppControl.DELETE -> onActionClick(AppAction.DELETE)
                        }
                    }
                )
            }
        }
    }
}

@Composable
internal fun AppStateControlButton(
    state: ApplicationOverview.State,
    onStart: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        ApplicationOverview.State.STOPPED -> {
            FilledTonalButton(onClick = onStart, modifier = modifier) {
                Text("Start")
            }
        }
        ApplicationOverview.State.ACTIVE -> {
            FilledTonalButton(onClick = onStop, modifier = modifier) {
                Text("Stop")
            }
        }
        ApplicationOverview.State.DEPLOYING -> {
            FilledTonalButton(onClick = onStop, enabled = false, modifier = modifier) {
                Text("Start")
            }
        }
    }
}

/**
 * Describes all possible actions the user can perform on a single app item.
 */
enum class AppAction {
    START,
    STOP,
    DELETE,
    EDIT,
    UPGRADE,
    ROLLBACK,
    SHELL,
    LOGS,
    WEB_PORTAL
}

@Preview
@Composable
fun ApplicationOverviewItemPreview() {
    MaterialTheme {
        ApplicationOverviewItem(
            applicationOverview = ApplicationOverview(
                name = "Jellyfin",
                version = "10.8.10_14.1.9",
                iconUrl = "https://github.com/jellyfin/jellyfin-ux/blob/master/branding/SVG/icon-transparent.svg",
                catalog = "Truenas",
                train = "Community",
                state = ApplicationOverview.State.ACTIVE,
                updateAvailable = false,
                webPortalUrl = "http://my.jellyfin.local/"
            ),
            onClick = { /* no-op */ },
            onActionClick = { /* no-op */ },
            modifier = Modifier.padding(16.dp)
        )
    }
}
