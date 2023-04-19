package com.boswelja.truemanager.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthComponents(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: AuthViewModel = koinViewModel()
) {
    val layoutDirection = LocalLayoutDirection.current
    val isLoading by viewModel.isLoading.collectAsState()

    var serverAddress by rememberSaveable { mutableStateOf("") }
    var selectedAuthType by rememberSaveable(saver = rememberAuthTypeSaver()) {
        mutableStateOf(AuthTypes.first())
    }

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var apiKey by rememberSaveable { mutableStateOf("") }

    val loginEnabled by remember {
        derivedStateOf {
            val authValid = when (selectedAuthType) {
                AuthType.ApiKeyAuth -> apiKey.isNotBlank()
                AuthType.BasicAuth -> username.isNotBlank() && password.isNotBlank()
            }
            serverAddress.isNotBlank() && authValid
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ServerAddressField(
            serverAddress = serverAddress,
            onServerAddressChange = { serverAddress = it },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    start = contentPadding.calculateStartPadding(layoutDirection),
                    end = contentPadding.calculateEndPadding(layoutDirection)
                )
                .widthIn(max = 560.dp)
        )
        AuthTypeSelector(
            currentType = selectedAuthType,
            onAuthTypeChange = { selectedAuthType = it },
            modifier = Modifier.padding(vertical = 16.dp),
            enabled = !isLoading
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 560.dp)
                .padding(
                    start = contentPadding.calculateStartPadding(layoutDirection),
                    end = contentPadding.calculateEndPadding(layoutDirection),
                    bottom = contentPadding.calculateBottomPadding()
                )
        ) {
            AnimatedContent(
                targetState = selectedAuthType,
                label = "Login method content",
                transitionSpec = { fadeIn() with fadeOut() },
            ) { authType ->
                when (authType) {
                    AuthType.ApiKeyAuth -> {
                        ApiKeyFields(
                            apiKey = apiKey,
                            onApiKeyChange = { apiKey = it },
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    AuthType.BasicAuth -> {
                        BasicAuthFields(
                            username = username,
                            onUsernameChange = { username = it },
                            password = password,
                            onPasswordChange = { password = it },
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LoginButton(
                onClick = {
                    when (selectedAuthType) {
                        AuthType.ApiKeyAuth -> viewModel.tryLogIn(serverAddress, apiKey)
                        AuthType.BasicAuth -> viewModel.tryLogIn(serverAddress, username, password)
                    }
                },
                enabled = loginEnabled,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerAddressField(
    serverAddress: String,
    onServerAddressChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextField(
        value = serverAddress,
        onValueChange = onServerAddressChange,
        label = { Text(stringResource(R.string.server_label)) },
        leadingIcon = { Icon(Icons.Default.Dns, contentDescription = null) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, autoCorrect = false),
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun AuthTypeSelector(
    currentType: AuthType,
    onAuthTypeChange: (AuthType) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    // TODO Segmented Buttons
    TabRow(
        selectedTabIndex = AuthTypes.indexOf(currentType),
        modifier = modifier
    ) {
        AuthTypes.forEach { authType ->
            Tab(
                selected = currentType == authType,
                onClick = { onAuthTypeChange(authType) },
                text = { Text(stringResource(authType.labelRes)) },
                icon = { Icon(imageVector = authType.icon, contentDescription = null) },
                enabled = enabled
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicAuthFields(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = username,
            onValueChange = onUsernameChange,
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
            onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.password_label)) },
            visualTransformation = remember { PasswordVisualTransformation() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiKeyFields(
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextField(
        value = apiKey,
        onValueChange = onApiKeyChange,
        label = { Text(stringResource(R.string.api_key_label)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            autoCorrect = false,
            capitalization = KeyboardCapitalization.None
        ),
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun LoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        Text(stringResource(R.string.log_in))
    }
}
