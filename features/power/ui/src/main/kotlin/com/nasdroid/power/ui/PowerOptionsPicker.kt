package com.nasdroid.power.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults.filledTonalButtonColors
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.design.MaterialThemeExt

/**
 * Displays various power-related options, such as "shut down" and "reboot".
 */
@Composable
fun PowerOptionsPicker(
    onShutdown: () -> Unit,
    onReboot: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var confirmationDialogType by rememberSaveable { mutableStateOf(ConfirmationDialogType.None) }
    Column(modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(enabled = enabled) {
                    confirmationDialogType = ConfirmationDialogType.Reboot
                }
                .heightIn(min = 48.dp)
                .fillMaxWidth()
        ) {
            Icon(Icons.Default.RestartAlt, null)
            Text(
                text = stringResource(R.string.power_command_reboot),
                style = MaterialThemeExt.typography.bodyLarge
            )
        }
        HorizontalDivider()
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(enabled = enabled) {
                    confirmationDialogType = ConfirmationDialogType.Shutdown
                }
                .heightIn(min = 48.dp)
                .fillMaxWidth()
        ) {
            Icon(Icons.Default.PowerSettingsNew, null)
            Text(
                text = stringResource(R.string.power_command_shutdown),
                style = MaterialThemeExt.typography.bodyLarge
            )
        }
    }
    when (confirmationDialogType) {
        ConfirmationDialogType.None -> { /* no-op */ }
        ConfirmationDialogType.Reboot -> {
            RebootConfirmationDialog(
                onDismiss = { confirmationDialogType = ConfirmationDialogType.None },
                onConfirm = {
                    confirmationDialogType = ConfirmationDialogType.None
                    onReboot()
                }
            )
        }
        ConfirmationDialogType.Shutdown -> {
            ShutdownConfirmationDialog(
                onDismiss = { confirmationDialogType = ConfirmationDialogType.None },
                onConfirm = {
                    confirmationDialogType = ConfirmationDialogType.None
                    onShutdown()
                }
            )
        }
    }
}

@Composable
internal fun ShutdownConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.PowerSettingsNew, null) },
        title = { Text(stringResource(R.string.power_confirm_shutdown_title)) },
        text = { Text(stringResource(R.string.power_confirm_shutdown_text)) },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.power_confirm_shutdown_negative))
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = onConfirm,
                colors = filledTonalButtonColors(
                    containerColor = MaterialThemeExt.colorScheme.errorContainer,
                    contentColor = MaterialThemeExt.colorScheme.onErrorContainer
                )
            ) {
                Text(stringResource(R.string.power_confirm_shutdown_positive))
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun RebootConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.RestartAlt, null) },
        title = { Text(stringResource(R.string.power_confirm_reboot_title)) },
        text = { Text(stringResource(R.string.power_confirm_reboot_text)) },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.power_confirm_reboot_negative))
            }
        },
        confirmButton = {
            FilledTonalButton(onClick = onConfirm) {
                Text(stringResource(R.string.power_confirm_reboot_positive))
            }
        },
        modifier = modifier,
    )
}

/**
 * Encapsulates all possible confirmation dialog types for [PowerOptionsPicker].
 */
enum class ConfirmationDialogType {
    None,
    Reboot,
    Shutdown
}

@Preview
@Composable
fun PowerOptionsPickerPreview() {
    PowerOptionsPicker(
        onShutdown = {},
        onReboot = {},
    )
}
