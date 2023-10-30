package com.nasdroid.auth.ui.register.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.ui.R

@Composable
fun AuthServerByKey(
    onLoginWithKey: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val (apiKey, onApiKeyChange) = rememberSaveable {
        mutableStateOf("")
    }
    val canLogIn by remember(apiKey) { derivedStateOf { apiKey.isNotBlank() } }

    Column(
        modifier = modifier
    ) {
        ApiKeyFields(
            apiKey = apiKey,
            onApiKeyChange = onApiKeyChange,
            onDone = {
                if (canLogIn) onLoginWithKey(apiKey)
            },
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        LoginButton(
            onClick = { onLoginWithKey(apiKey) },
            enabled = canLogIn,
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
        modifier = modifier
    )
}
