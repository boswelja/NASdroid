package com.boswelja.truemanager.auth.ui.addserver

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Label
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.auth.ui.R
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthComponents(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: AddServerViewModel = koinViewModel()
) {
    val layoutDirection = LocalLayoutDirection.current
    val isLoading by viewModel.isLoading.collectAsState()

    var serverName by rememberSaveable { mutableStateOf("") }
    var serverAddress by rememberSaveable { mutableStateOf("") }
    var selectedAuthType by rememberSaveable(saver = rememberAuthTypeSaver()) {
        mutableStateOf(AuthTypes.first())
    }

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var apiKey by rememberSaveable { mutableStateOf("") }
    var isServerAddressInvalid by rememberSaveable {
        mutableStateOf(false)
    }
    var isUsernameOrPasswordInvalid by rememberSaveable {
        mutableStateOf(false)
    }
    var isApiKeyInvalid by rememberSaveable {
        mutableStateOf(false)
    }

    val logIn = {
        when (selectedAuthType) {
            AuthType.ApiKeyAuth -> viewModel.tryLogIn(serverName, serverAddress, apiKey)
            AuthType.BasicAuth -> viewModel.tryLogIn(serverName, serverAddress, username, password)
        }
    }
    val loginEnabled by remember {
        derivedStateOf {
            val authValid = when (selectedAuthType) {
                AuthType.ApiKeyAuth -> apiKey.isNotBlank() && !isApiKeyInvalid
                AuthType.BasicAuth -> username.isNotBlank() && password.isNotBlank() && !isUsernameOrPasswordInvalid
            }
            !isLoading && serverAddress.isNotBlank() && authValid && !isServerAddressInvalid
        }
    }

    // TODO handle login properly
    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest {
            when (it) {
                AddServerViewModel.Event.LoginSuccess -> onLoginSuccess()
                AddServerViewModel.Event.LoginFailedNotHttps,
                AddServerViewModel.Event.LoginFailedServerNotFound ->
                    isServerAddressInvalid = true
                AddServerViewModel.Event.LoginFailedKeyInvalid ->
                    isApiKeyInvalid = true
                AddServerViewModel.Event.LoginFailedUsernameOrPasswordInvalid ->
                    isUsernameOrPasswordInvalid = true
                AddServerViewModel.Event.LoginFailedKeyAlreadyExists -> TODO()
                null -> return@collectLatest
            }
            viewModel.clearPendingEvent()
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ServerNameField(
            serverName = serverName,
            onServerNameChange = { serverName = it },
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
        Spacer(Modifier.height(8.dp))
        ServerAddressField(
            serverAddress = serverAddress,
            onServerAddressChange = {
                serverAddress = it
                isServerAddressInvalid = false
            },
            enabled = !isLoading,
            error = isServerAddressInvalid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
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
                transitionSpec = { fadeIn() togetherWith fadeOut() },
            ) { authType ->
                when (authType) {
                    AuthType.ApiKeyAuth -> {
                        ApiKeyFields(
                            apiKey = apiKey,
                            onApiKeyChange = {
                                apiKey = it
                                isApiKeyInvalid = false
                            },
                            onDone = logIn,
                            enabled = !isLoading,
                            error = isApiKeyInvalid,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    AuthType.BasicAuth -> {
                        BasicAuthFields(
                            username = username,
                            onUsernameChange = {
                                username = it
                                isUsernameOrPasswordInvalid = false
                            },
                            password = password,
                            onPasswordChange = {
                                password = it
                                isUsernameOrPasswordInvalid = false
                            },
                            onDone = logIn,
                            enabled = !isLoading,
                            error = isUsernameOrPasswordInvalid,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LoginButton(
                onClick = logIn,
                enabled = loginEnabled,
                loading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ServerNameField(
    serverName: String,
    onServerNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean
) {
    TextField(
        value = serverName,
        onValueChange = onServerNameChange,
        label = { Text("Server Name (Optional)") },
        leadingIcon = { Icon(Icons.Default.Label, contentDescription = null) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun ServerAddressField(
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
        supportingText = if (error) {{
            Text(stringResource(R.string.invalid_server_address))
        }} else null,
        modifier = modifier
    )
}

@Composable
fun BasicAuthFields(
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
