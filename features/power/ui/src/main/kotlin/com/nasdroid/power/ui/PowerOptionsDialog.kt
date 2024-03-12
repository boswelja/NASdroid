package com.nasdroid.power.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel

/**
 * Displays various power-related options, such as "shut down" and "reboot". This overload handles
 * sending the power-related commands to the server, as well as displaying the command results.
 */
@Composable
fun PowerOptionsDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PowerOptionsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    PowerOptionsDialog(
        onDismiss = onDismiss,
        onShutdown = viewModel::shutdown,
        onReboot = viewModel::reboot,
        state = state,
        modifier = modifier
    )
}

@Composable
internal fun PowerOptionsDialog(
    onDismiss: () -> Unit,
    onShutdown: () -> Unit,
    onReboot: () -> Unit,
    state: PowerOptionsState?,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        confirmButton = {
            TextButton(onClick = onDismiss, enabled = state != PowerOptionsState.Loading) {
                Text(stringResource(R.string.power_dialog_cancel))
            }
        },
        title = {
            Text(stringResource(R.string.power_dialog_title))
        },
        text = {
            AnimatedContent(targetState = state, label = "Dialog content") { powerOptionsState ->
                when (powerOptionsState) {
                    is PowerOptionsState.ErrorGeneric -> {
                        Text(stringResource(R.string.power_command_error_unknown))
                    }
                    PowerOptionsState.ErrorUnauthorized -> {
                        Text(stringResource(R.string.power_command_error_unauthorized))
                    }
                    PowerOptionsState.Loading -> {
                        Box(Modifier.fillMaxWidth()) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    }
                    null -> {
                        PowerOptionsPicker(
                            onShutdown = onShutdown,
                            onReboot = onReboot,
                            enabled = state == null
                        )
                    }
                }
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun PowerOptionsDialogPreview() {
    var state: PowerOptionsState? by remember { mutableStateOf(null) }
    Box(Modifier.fillMaxSize()) {
        PowerOptionsDialog(
            onDismiss = { state = PowerOptionsState.Loading },
            onShutdown = {
                state = PowerOptionsState.ErrorUnauthorized
            },
            onReboot = {
                state = PowerOptionsState.ErrorGeneric(
                    "This is a preview of technical issues"
                )
            },
            state = state,
        )
    }
}
