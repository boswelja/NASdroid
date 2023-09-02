package com.boswelja.truemanager.auth.ui.addserver

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

/**
 * A screen that allows the user to add a new registered server.
 */
@Composable
fun AddServerScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: AddServerViewModel = koinViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()

    AddServerContent(
        loading = isLoading,
        apiKeyLogIn = viewModel::tryLogIn,
        basicLogIn = viewModel::tryLogIn,
        modifier = modifier,
        contentPadding = contentPadding,
    )
}

@Composable
fun AddServerContent(
    loading: Boolean,
    apiKeyLogIn: (serverName: String, address: String, key: String) -> Unit,
    basicLogIn: (serverName: String, address: String, username: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val layoutDirection = LocalLayoutDirection.current

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
            AuthType.ApiKeyAuth -> apiKeyLogIn(serverName, serverAddress, apiKey)
            AuthType.BasicAuth -> basicLogIn(serverName, serverAddress, username, password)
        }
    }
    val loginEnabled by remember {
        derivedStateOf {
            val authValid = when (selectedAuthType) {
                AuthType.ApiKeyAuth -> apiKey.isNotBlank() && !isApiKeyInvalid
                AuthType.BasicAuth -> username.isNotBlank() && password.isNotBlank() && !isUsernameOrPasswordInvalid
            }
            !loading && serverAddress.isNotBlank() && authValid && !isServerAddressInvalid
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ServerNameField(
            serverName = serverName,
            onServerNameChange = { serverName = it },
            enabled = !loading,
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
            enabled = !loading,
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
            enabled = !loading
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
                            enabled = !loading,
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
                            enabled = !loading,
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
                loading = loading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
