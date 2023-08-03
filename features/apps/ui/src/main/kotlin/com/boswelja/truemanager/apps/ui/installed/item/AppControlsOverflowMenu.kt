package com.boswelja.truemanager.apps.ui.installed.item

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.boswelja.truemanager.apps.logic.installed.ApplicationOverview
import com.boswelja.truemanager.apps.ui.R

@Composable
internal fun AppControlsOverflowMenu(
    app: ApplicationOverview,
    onControlClick: (AppControl) -> Unit,
    modifier: Modifier = Modifier,
    controls: Iterable<AppControl> = AppControl.entries
) {
    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        var expanded by remember { mutableStateOf(false) }
        IconButton(onClick = { expanded = true }, modifier = modifier) {
            Icon(Icons.Default.MoreVert, null)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            controls.forEach { control ->
                val enabled = remember(app) {
                    control.enabled(app)
                }
                DropdownMenuItem(
                    text = { Text(stringResource(control.titleRes)) },
                    leadingIcon = { Icon(control.icon, null) },
                    onClick = {
                        onControlClick(control)
                        expanded = false
                    },
                    enabled = enabled
                )
            }
        }
    }
}

/**
 * All available "controls" for the app overflow menu.
 *
 * @property titleRes A String resource for the items title.
 * @property icon The items icon.
 * @property enabled A function to calculate whether an item is enabled for the app.
 */
enum class AppControl(
    @StringRes val titleRes: Int,
    val icon: ImageVector,
    val enabled: (ApplicationOverview) -> Boolean,
) {
    UPGRADE(
        titleRes = R.string.app_control_upgrade,
        icon = Icons.Default.Upgrade,
        enabled = { it.updateAvailable }
    ),
    ROLL_BACK(
        titleRes = R.string.app_control_rollback,
        icon = Icons.Default.Restore,
        enabled = { true }
    ),
    EDIT(
        titleRes = R.string.app_control_edit,
        icon = Icons.Default.Edit,
        enabled = { true }
    ),
    SHELL(
        titleRes = R.string.app_control_shell,
        icon = Icons.Default.Terminal,
        enabled = { true }
    ),
    LOGS(
        titleRes = R.string.app_control_logs,
        icon = Icons.Default.TextSnippet,
        enabled = { it.state == ApplicationOverview.State.ACTIVE }
    ),
    DELETE(
        titleRes = R.string.app_control_delete,
        icon = Icons.Default.Delete,
        enabled = { true }
    )
}
