package com.nasdroid.auth.ui.register.find

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.ui.R
import com.nasdroid.design.MaterialThemeExt

@Composable
internal fun FindServerByAddress(
    onServerAddressChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val (address, onAddressChange) = rememberSaveable { mutableStateOf("") }
    val canConnect by remember(address) { derivedStateOf { address.isNotBlank() } }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.medium)
    ) {
        Text(
            text = "Connect to a server via a custom address",
            style = MaterialThemeExt.typography.titleMedium
        )
        ServerAddressField(
            serverAddress = address,
            onServerAddressChange = onAddressChange,
            onKeyboardAction = {
                if (canConnect) onServerAddressChange(address)
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        Button(
            onClick = { onServerAddressChange(address) },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = canConnect
        ) {
            Text(stringResource(R.string.connect_server))
        }
    }
}

@Composable
internal fun ServerAddressField(
    serverAddress: String,
    onServerAddressChange: (String) -> Unit,
    onKeyboardAction: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: Boolean = false
) {
    TextField(
        value = serverAddress,
        onValueChange = onServerAddressChange,
        label = { Text(stringResource(R.string.server_label)) },
        leadingIcon = { Icon(Icons.Default.Dns, contentDescription = null) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Uri,
            autoCorrect = false,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions { onKeyboardAction() },
        singleLine = true,
        enabled = enabled,
        isError = error,
        supportingText = if (error) {
            {
                Text(stringResource(R.string.invalid_server_address))
            }
        } else {
            null
        },
        modifier = modifier
    )
}

@PreviewFontScale
@Composable
fun FindServerByAddressPreview() {
    MaterialThemeExt {
        Surface {
            FindServerByAddress(onServerAddressChange = {}, modifier = Modifier.padding(16.dp))
        }
    }
}