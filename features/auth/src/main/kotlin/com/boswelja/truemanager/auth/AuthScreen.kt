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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    // TODO Different layouts based on device type
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        AuthComponents(Modifier.fillMaxSize(), contentPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
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

    Column(modifier = modifier) {
        TextField(
            value = serverAddress,
            onValueChange = { serverAddress = it },
            label = { Text(stringResource(R.string.server_label)) },
            leadingIcon = { Icon(Icons.Default.Dns, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, autoCorrect = false),
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    start = contentPadding.calculateStartPadding(layoutDirection),
                    end = contentPadding.calculateEndPadding(layoutDirection)
                )
        )
        // TODO Segmented Buttons
        TabRow(
            selectedTabIndex = AuthTypes.indexOf(selectedAuthType),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            AuthTypes.forEach {
                Tab(
                    selected = selectedAuthType == it,
                    onClick = { selectedAuthType = it },
                    text = { Text(stringResource(it.labelRes)) },
                    icon = { Icon(imageVector = it.icon, contentDescription = null) }
                )
            }
        }
        AnimatedContent(
            targetState = selectedAuthType,
            label = "Login method content",
            transitionSpec = { fadeIn() with fadeOut() },
            modifier = Modifier
                .padding(
                    start = contentPadding.calculateStartPadding(layoutDirection),
                    end = contentPadding.calculateEndPadding(layoutDirection)
                )
        ) { authType ->
            when (authType) {
                AuthType.ApiKeyAuth -> {
                    TextField(
                        value = apiKey,
                        onValueChange = { apiKey = it },
                        label = { Text(stringResource(R.string.api_key_label)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            autoCorrect = false,
                            capitalization = KeyboardCapitalization.None
                        ),
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 560.dp)
                    )
                }
                AuthType.BasicAuth -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 560.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text(stringResource(R.string.username_label)) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                autoCorrect = false,
                                capitalization = KeyboardCapitalization.None
                            ),
                            enabled = !isLoading,
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
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                when (selectedAuthType) {
                    AuthType.ApiKeyAuth -> viewModel.tryLogIn(serverAddress, apiKey)
                    AuthType.BasicAuth -> viewModel.tryLogIn(serverAddress, username, password)
                }
            },
            enabled = loginEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = contentPadding.calculateStartPadding(layoutDirection),
                    end = contentPadding.calculateEndPadding(layoutDirection),
                    bottom = contentPadding.calculateBottomPadding()
                )
        ) {
            Text(stringResource(R.string.log_in))
        }
    }
}
