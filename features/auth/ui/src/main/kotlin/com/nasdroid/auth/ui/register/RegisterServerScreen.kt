package com.nasdroid.auth.ui.register

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.ui.R
import com.nasdroid.design.MaterialThemeExt
import org.koin.androidx.compose.koinViewModel

/**
 * A screen that allows the user to quickly find and connect to their TrueNAS server.
 */
@Composable
fun RegisterServerScreen(
    onServerRegistered: () -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: RegisterServerViewModel = koinViewModel()
) {
    val registerState by viewModel.registerState.collectAsState()
    var serverAddress by remember {
        mutableStateOf("")
    }
    var authData by remember {
        mutableStateOf<AuthData>(AuthData.ApiKey(""))
    }

    val isLoading = registerState == RegisterState.Loading || registerState == RegisterState.Success

    LaunchedEffect(registerState) {
        if (registerState is RegisterState.Success) {
            onServerRegistered()
        }
    }

    RegisterServerContent(
        serverAddress = serverAddress,
        onServerAddressChange = { newAuthData ->
            serverAddress = newAuthData
            viewModel.clearPendingState()
        },
        authData = authData,
        onAuthDataChange = { newAuthData ->
            authData = newAuthData
            viewModel.clearPendingState()
        },
        onRegisterClick = {
            authData.let { auth ->
                when (auth) {
                    is AuthData.ApiKey -> viewModel.tryRegisterServer(
                        serverAddress = serverAddress,
                        apiKey = auth.key
                    )
                    is AuthData.Basic -> viewModel.tryRegisterServer(
                        serverAddress = serverAddress,
                        username = auth.username,
                        password = auth.password
                    )
                }
            }
        },
        registerEnabled = serverAddress.isNotBlank() && !isLoading,
        windowSizeClass = windowSizeClass,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .then(modifier),
        contentPadding = contentPadding,
        loading = isLoading,
        serverAddressError = registerState is RegisterState.AddressError,
        authDataError = registerState is RegisterState.AuthError
    )
}

/**
 * The main content of [RegisterServerScreen]. This content will adapt to various form factors.
 */
@Composable
fun RegisterServerContent(
    serverAddress: String,
    onServerAddressChange: (String) -> Unit,
    authData: AuthData,
    onAuthDataChange: (AuthData) -> Unit,
    onRegisterClick: () -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    serverAddressError: Boolean = false,
    authDataError: Boolean = false,
    loading: Boolean = false,
    registerEnabled: Boolean = true,
) {
    when {
        windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact &&
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact -> {
            RegisterServerHorizontalContent(
                serverAddress = serverAddress,
                onServerAddressChange = onServerAddressChange,
                authData = authData,
                onAuthDataChange = onAuthDataChange,
                onRegisterClick = onRegisterClick,
                registerEnabled = registerEnabled,
                modifier = modifier.padding(contentPadding),
                serverAddressError = serverAddressError,
                authDataError = authDataError,
                loading = loading
            )
        }
        windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact &&
                windowSizeClass.heightSizeClass > WindowHeightSizeClass.Compact -> {
            RegisterServerCenteredContent(
                serverAddress = serverAddress,
                onServerAddressChange = onServerAddressChange,
                authData = authData,
                onAuthDataChange = onAuthDataChange,
                onRegisterClick = onRegisterClick,
                registerEnabled = registerEnabled,
                modifier = modifier.padding(contentPadding),
                serverAddressError = serverAddressError,
                authDataError = authDataError,
                loading = loading
            )
        }
        else -> {
            RegisterServerVerticalContent(
                serverAddress = serverAddress,
                onServerAddressChange = onServerAddressChange,
                authData = authData,
                onAuthDataChange = onAuthDataChange,
                onRegisterClick = onRegisterClick,
                registerEnabled = registerEnabled,
                modifier = modifier.padding(contentPadding),
                serverAddressError = serverAddressError,
                authDataError = authDataError,
                loading = loading
            )
        }
    }
}

/**
 * The content for [RegisterServerScreen], laid out vertically for a portrait phone form factor.
 */
@Composable
fun RegisterServerVerticalContent(
    serverAddress: String,
    onServerAddressChange: (String) -> Unit,
    authData: AuthData,
    onAuthDataChange: (AuthData) -> Unit,
    onRegisterClick: () -> Unit,
    registerEnabled: Boolean,
    modifier: Modifier = Modifier,
    serverAddressError: Boolean = false,
    authDataError: Boolean = false,
    loading: Boolean = false,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.large, Alignment.Bottom)
    ) {
        ServerAddressField(
            address = serverAddress,
            onAddressChange = onServerAddressChange,
            modifier = Modifier.widthIn(max = 480.dp),
            error = serverAddressError,
            enabled = !loading
        )
        AuthFields(
            authData = authData,
            onAuthDataChange = onAuthDataChange,
            onDone = { if (registerEnabled) onRegisterClick() },
            modifier = Modifier.widthIn(max = 480.dp),
            error = authDataError,
            enabled = !loading
        )
        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .widthIn(480.dp)
                .fillMaxWidth(),
            enabled = registerEnabled,
        ) {
            Text(stringResource(R.string.connect_server))
        }
    }
}

/**
 * The content for [RegisterServerScreen], laid out horizontally for a landscape phone form factor.
 */
@Composable
fun RegisterServerHorizontalContent(
    serverAddress: String,
    onServerAddressChange: (String) -> Unit,
    authData: AuthData,
    onAuthDataChange: (AuthData) -> Unit,
    onRegisterClick: () -> Unit,
    registerEnabled: Boolean,
    modifier: Modifier = Modifier,
    serverAddressError: Boolean = false,
    authDataError: Boolean = false,
    loading: Boolean = false,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.large, Alignment.CenterVertically)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.large)
        ) {
            ServerAddressField(
                address = serverAddress,
                onAddressChange = onServerAddressChange,
                modifier = Modifier.weight(1f),
                error = serverAddressError,
                enabled = !loading
            )
            AuthFields(
                authData = authData,
                onAuthDataChange = onAuthDataChange,
                onDone = { if (registerEnabled) onRegisterClick() },
                modifier = Modifier.weight(1f),
                error = authDataError,
                enabled = !loading
            )
        }
        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth(),
            enabled = registerEnabled,
        ) {
            Text(stringResource(R.string.connect_server))
        }
    }
}

/**
 * The content for [RegisterServerScreen], laid out vertically in the center of the screen for a
 * large-screen devices.
 */
@Composable
fun RegisterServerCenteredContent(
    serverAddress: String,
    onServerAddressChange: (String) -> Unit,
    authData: AuthData,
    onAuthDataChange: (AuthData) -> Unit,
    onRegisterClick: () -> Unit,
    registerEnabled: Boolean,
    modifier: Modifier = Modifier,
    serverAddressError: Boolean = false,
    authDataError: Boolean = false,
    loading: Boolean = false,
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        ElevatedCard {
            Column(
                modifier = Modifier.padding(MaterialThemeExt.paddings.large),
                verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.large, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ServerAddressField(
                    address = serverAddress,
                    onAddressChange = onServerAddressChange,
                    modifier = Modifier.widthIn(max = 480.dp),
                    error = serverAddressError,
                    enabled = !loading
                )
                AuthFields(
                    authData = authData,
                    onAuthDataChange = onAuthDataChange,
                    modifier = Modifier.widthIn(max = 480.dp),
                    error = authDataError,
                    onDone = { if (registerEnabled) onRegisterClick() },
                    enabled = !loading
                )
                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth(),
                    enabled = registerEnabled,
                ) {
                    Text(stringResource(R.string.connect_server))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.S)
@PreviewLightDark
@PreviewScreenSizes
@Composable
fun RegisterServerScreenPreview() {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = remember(configuration) {
        val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
        WindowSizeClass.calculateFromSize(size)
    }

    var serverAddress by remember {
        mutableStateOf("")
    }
    var authData by remember {
        mutableStateOf<AuthData>(AuthData.ApiKey(""))
    }
    MaterialThemeExt(
        colorScheme = if (isSystemInDarkTheme()) {
            dynamicDarkColorScheme(context)
        } else {
            dynamicLightColorScheme(context)
        }
    ) {
        Surface {
            RegisterServerContent(
                serverAddress = serverAddress,
                onServerAddressChange = { serverAddress = it },
                authData = authData,
                onAuthDataChange = { authData = it },
                onRegisterClick = {},
                registerEnabled = true,
                windowSizeClass = windowSizeClass,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialThemeExt.colorScheme.background),
                contentPadding = PaddingValues(MaterialThemeExt.paddings.large)
            )
        }
    }
}
