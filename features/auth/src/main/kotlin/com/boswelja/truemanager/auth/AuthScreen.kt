package com.boswelja.truemanager.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.compose.rememberKoinInject

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel(),
) {
    // TODO Different layouts based on device type
    val isLoading by viewModel.isLoading.collectAsState()

    // TODO Proper handling of auth success
    val apiStateProvider: ApiStateProvider = rememberKoinInject()
    LaunchedEffect(isLoading) {
        if (!isLoading && apiStateProvider.sessionToken != null) onLoginSuccess()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        AuthFields(
            onLogIn = { serverAddress, username, password ->
                viewModel.tryLogIn(serverAddress, username, password)
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 560.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthFields(
    onLogIn: (serverAddress: String, username: String, password: String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var serverAddress by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val loginEnabled by remember(enabled) {
        derivedStateOf {
            enabled && serverAddress.isNotBlank() && username.isNotBlank() && password.isNotBlank()
        }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = serverAddress,
            onValueChange = { serverAddress = it },
            label = { Text(stringResource(R.string.server_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, autoCorrect = false),
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.username_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrect = false,
                capitalization = KeyboardCapitalization.None
            ),
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password_label)) },
            visualTransformation = remember { PasswordVisualTransformation() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onLogIn(serverAddress, username, password) },
            enabled = loginEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.log_in))
        }
    }
}

@Preview
@Composable
fun AuthFieldsPreview() {
    var enabled by remember { mutableStateOf(false) }
    AuthFields(
        onLogIn = { _, _, _ -> enabled = true },
        enabled = enabled
    )
}
