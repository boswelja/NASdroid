package com.nasdroid.auth.ui.register.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.ui.R

@Composable
fun AuthServerScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val (apiKey, onApiKeyChange) = rememberSaveable {
        mutableStateOf("")
    }
    Column(modifier.padding(contentPadding)) {
        ApiKeyFields(apiKey = apiKey, onApiKeyChange = onApiKeyChange, onDone = onLoginSuccess)
        LoginButton(onClick = onLoginSuccess)
    }
}

@Composable
fun ApiKeyFields(
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: Boolean = false
) {
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
        }} else null,
        modifier = modifier
    )
}

@Composable
fun LoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        if (loading) {
            CircularProgressIndicator(Modifier.size(26.dp))
        } else {
            Text(stringResource(R.string.log_in))
        }
    }
}
