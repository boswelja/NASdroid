package com.nasdroid.auth.ui.register

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.nasdroid.auth.ui.R

/**
 * A text field that allows the user to input an API key for authentication.
 */
@Composable
fun ApiKeyField(
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: Boolean = false
) {
    var isKeyHidden by rememberSaveable {
        mutableStateOf(true)
    }
    TextField(
        value = apiKey,
        onValueChange = onApiKeyChange,
        label = { Text(stringResource(R.string.api_key_label)) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Password,
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
        visualTransformation = if (isKeyHidden) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = {
            IconButton(onClick = { isKeyHidden = !isKeyHidden }) {
                if (isKeyHidden) {
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
