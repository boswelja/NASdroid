package com.nasdroid.auth.ui.serverselect

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.KeyOff
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.nasdroid.auth.ui.R

/**
 * Displays a dialog to communicate a [LoginState.Error] to the user.
 */
@Composable
fun SelectServerErrorDialog(
    onDismissRequest: () -> Unit,
    error: LoginState.Error,
    modifier: Modifier = Modifier
) {
    when (error) {
        LoginState.Error.CredentialsInvalid -> {
            GenericErrorDialog(
                icon = Icons.Default.KeyOff,
                title = stringResource(R.string.error_invalid_credentials_title),
                text = stringResource(R.string.error_invalid_credentials_text),
                onDismissRequest = onDismissRequest,
                modifier = modifier
            )
        }
        LoginState.Error.Generic -> {
            GenericErrorDialog(
                icon = Icons.Default.Error,
                title = stringResource(R.string.error_unknown_title),
                text = stringResource(R.string.error_unknown_text),
                onDismissRequest = onDismissRequest,
                modifier = modifier
            )
        }
        LoginState.Error.ServerUnreachable -> {
            GenericErrorDialog(
                icon = Icons.Default.LinkOff,
                title = stringResource(R.string.error_server_unreachable_title),
                text = stringResource(R.string.error_server_unreachable_text),
                onDismissRequest = onDismissRequest,
                modifier = modifier
            )
        }
    }
}

@Composable
internal fun GenericErrorDialog(
    icon: ImageVector,
    title: String,
    text: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = { Icon(icon, contentDescription = null) },
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("OK")
            }
        },
        modifier = modifier
    )
}
