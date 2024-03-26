package com.nasdroid.auth.ui.register.find

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.ui.R
import com.nasdroid.design.MaterialThemeExt

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun FindServerByAddress(
    address: String,
    onAddressChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.medium)
    ) {
        Text(
            text = "Enter the network address of the server",
            style = MaterialThemeExt.typography.titleMedium
        )
        Column {
            ServerAddressField(
                serverAddress = address,
                onServerAddressChange = onAddressChange,
                modifier = Modifier.fillMaxWidth(),
                error = error,
                enabled = enabled
            )
            AnimatedVisibility(
                visible = address.isEmpty(),
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.small)
                ) {
                    DEFAULT_SUGGESTIONS.forEach { suggestedAddress ->
                        SuggestionChip(
                            onClick = { onAddressChange(suggestedAddress) },
                            label = {
                                Text(
                                    text = suggestedAddress,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

internal val DEFAULT_SUGGESTIONS = listOf(
    "http://truenas.local/"
)

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
            imeAction = ImeAction.Next
        ),
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
    var address by remember {
        mutableStateOf("")
    }
    MaterialThemeExt {
        Surface {
            FindServerByAddress(
                address = address,
                onAddressChange = { address = it },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}