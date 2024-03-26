package com.nasdroid.auth.ui.register.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.ui.R

/**
 * An opinionated set of Composables that asks the user for an API key, and submits it via
 * [onLoginWithKey] when they are done.
 */
@Composable
fun AuthServerByKey(
    onLoginWithKey: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val (apiKey, onApiKeyChange) = rememberSaveable {
        mutableStateOf("")
    }
    val canLogIn by remember(apiKey, enabled) { derivedStateOf { enabled && apiKey.isNotBlank() } }

    Column(
        modifier = modifier
    ) {
        ApiKeyFields(
            apiKey = apiKey,
            onApiKeyChange = onApiKeyChange,
            onDone = {
                if (canLogIn) onLoginWithKey(apiKey)
            },
            enabled = enabled,
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
internal fun ApiKeyFields(
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: Boolean = false
) {
    var isHidden by rememberSaveable {
        mutableStateOf(true)
    }
    TextField(
        value = apiKey,
        onValueChange = onApiKeyChange,
        label = { Text(stringResource(R.string.api_key_label)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            autoCorrect = false,
            capitalization = KeyboardCapitalization.None,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions {
            defaultKeyboardAction(ImeAction.Done)
            onDone()
        },
        singleLine = true,
        enabled = enabled,
        isError = error,
        supportingText = if (error) {{
            Text(stringResource(R.string.invalid_key_auth))
        }} else { null },
        visualTransformation = if (isHidden) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = {
            IconButton(onClick = { isHidden = !isHidden }) {
                if (isHidden) {
                    Icon(Icons.Default.Visibility, null)
                } else {
                    Icon(Icons.Default.VisibilityOff, null)
                }
            }
        },
        leadingIcon = {
            Icon(Icons.Default.Key, null)
        },
        modifier = modifier
    )
}
