package com.nasdroid.auth.ui.register.find

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.ui.R

/**
 * A screen that allows the user to quickly find and connect to their TrueNAS server.
 */
@Composable
fun FindServerScreen(
    onServerFound: (address: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    FindServerByAddress(
        onServerAddressChange = onServerFound,
        modifier = modifier.padding(contentPadding)
    )
}

@Composable
internal fun FindServerByAddress(
    onServerAddressChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        val (address, onAddressChange) = rememberSaveable { mutableStateOf("") }
        val canConnect by remember(address) { derivedStateOf { address.isNotBlank() }}

        // TODO This is just a temporary title. It should be a proper TopAppBar with a back button.
        Text(
            text = "Add Server",
            style = MaterialTheme.typography.displayMedium
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            ServerAddressField(
                serverAddress = address,
                onServerAddressChange = onAddressChange,
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { onServerAddressChange(address) },
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth(),
                enabled = canConnect
            ) {
                Text(stringResource(R.string.connect_server))
            }
        }
    }
}

@Composable
internal fun ServerAddressField(
    serverAddress: String,
    onServerAddressChange: (String) -> Unit,
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
        singleLine = true,
        enabled = enabled,
        isError = error,
        supportingText = if (error) {{
            Text(stringResource(R.string.invalid_server_address))
        }} else {
            null
        },
        modifier = modifier
    )
}

@PreviewScreenSizes
@Composable
fun FindServerByAddressPreview() {
    FindServerByAddress(onServerAddressChange = {}, modifier = Modifier
        .fillMaxSize()
        .padding(16.dp))
}
