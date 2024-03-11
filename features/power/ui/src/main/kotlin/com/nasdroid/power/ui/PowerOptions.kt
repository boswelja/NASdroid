package com.nasdroid.power.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults.filledTonalButtonColors
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PowerOptions(
    viewModel: PowerOptionsViewModel,
    modifier: Modifier = Modifier
) {
    PowerOptions(
        onShutdown = viewModel::shutdown,
        onReboot = viewModel::reboot,
        modifier = modifier
    )
}

@Composable
fun PowerOptions(
    onShutdown: () -> Unit,
    onReboot: () -> Unit,
    modifier: Modifier = Modifier
) {
    var confirmationDialog by rememberSaveable { mutableStateOf(ConfirmationDialog.None) }
    Column(modifier) {
        ListItem(
            headlineContent = { Text("Reboot") },
            leadingContent = { Icon(Icons.Default.RestartAlt, null) },
            modifier = Modifier.clickable { confirmationDialog = ConfirmationDialog.Reboot }
        )
        ListItem(
            headlineContent = { Text("Shutdown") },
            leadingContent = { Icon(Icons.Default.PowerSettingsNew, null) },
            modifier = Modifier.clickable { confirmationDialog = ConfirmationDialog.Shutdown }
        )
    }
    when (confirmationDialog) {
        ConfirmationDialog.None -> { /* no-op */ }
        ConfirmationDialog.Reboot -> {
            RebootConfirmationDialog(
                onDismiss = { confirmationDialog = ConfirmationDialog.None },
                onConfirm = onReboot
            )
        }
        ConfirmationDialog.Shutdown -> {
            ShutdownConfirmationDialog(
                onDismiss = { confirmationDialog = ConfirmationDialog.None },
                onConfirm = onShutdown
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
        title = { Text("Shutdown") },
        text = { Text("Are you sure you want to shut down the system?") },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = onConfirm,
                colors = filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Confirm")
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
        title = { Text("Reboot") },
        text = { Text("Are you sure you want to reboot the system?") },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        confirmButton = {
            FilledTonalButton(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        modifier = modifier,
    )
}
enum class ConfirmationDialog {
    None,
    Reboot,
    Shutdown
}

@Preview
@Composable
fun PowerOptionsPreview() {
    PowerOptions(
        onShutdown = {},
        onReboot = {},
    )
}
