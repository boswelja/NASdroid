package com.nasdroid.auth.ui.register.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.ui.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthServerScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: AuthServerViewModel = koinViewModel()
) {
    val loginState by viewModel.loginState.collectAsState()
    LaunchedEffect(loginState) {
        if (loginState == LoginState.Success) {
            onLoginSuccess()
        }
    }

    AuthServerByKey(
        onLoginWithKey = {
            viewModel.logIn(it)
        },
        modifier = modifier.padding(contentPadding)
    )
}

@Composable
internal fun AuthServerByKey(
    onLoginWithKey: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val (apiKey, onApiKeyChange) = rememberSaveable {
        mutableStateOf("")
    }
    val canLogIn by remember(apiKey) { derivedStateOf { apiKey.isNotBlank() } }

    Column(modifier) {
        // TODO This is just a temporary title. It should be a proper TopAppBar with a back button.
        Text(
            text = "Log In",
            style = MaterialTheme.typography.displayMedium
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f),
            verticalArrangement = Arrangement.Center
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
        }} else null,
        modifier = modifier
    )
}

@Composable
internal fun BasicAuthFields(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: Boolean = false,
) {
    Column(modifier) {
        AnimatedVisibility(
            visible = error,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.error) {
                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Error, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.invalid_basic_auth),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        TextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text(stringResource(R.string.username_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrect = false,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.password_label)) },
            visualTransformation = remember { PasswordVisualTransformation() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions {
                defaultKeyboardAction(ImeAction.Done)
                onDone()
            },
            singleLine = true,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
internal fun LoginButton(
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
