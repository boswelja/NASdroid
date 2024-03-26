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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
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
    var serverAddress by remember {
        mutableStateOf("")
    }
    var authData by remember {
        mutableStateOf<AuthData>(AuthData.ApiKey(""))
    }
    FindServerContent(
        serverAddress = serverAddress,
        onServerAddressChange = { serverAddress = it },
        authData = authData,
        onAuthDataChange = { authData = it },
        windowSizeClass = windowSizeClass,
        modifier = modifier,
        contentPadding = contentPadding
    )
}

@Composable
fun FindServerContent(
    serverAddress: String,
    onServerAddressChange: (String) -> Unit,
    authData: AuthData,
    onAuthDataChange: (AuthData) -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    when {
        windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact &&
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact -> {
            FindServerHorizontalContent(
                serverAddress = serverAddress,
                onServerAddressChange = onServerAddressChange,
                authData = authData,
                onAuthDataChange = onAuthDataChange,
                modifier = modifier.padding(contentPadding)
            )
        }
        windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact &&
                windowSizeClass.heightSizeClass > WindowHeightSizeClass.Compact -> {
            FindServerCenteredContent(
                serverAddress = serverAddress,
                onServerAddressChange = onServerAddressChange,
                authData = authData,
                onAuthDataChange = onAuthDataChange,
                modifier = modifier.padding(contentPadding)
            )
        }
        else -> {
            FindServerVerticalContent(
                serverAddress = serverAddress,
                onServerAddressChange = onServerAddressChange,
                authData = authData,
                onAuthDataChange = onAuthDataChange,
                modifier = modifier.padding(contentPadding)
            )
        }
    }
}

@Composable
fun FindServerVerticalContent(
    serverAddress: String,
    onServerAddressChange: (String) -> Unit,
    authData: AuthData,
    onAuthDataChange: (AuthData) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.large, Alignment.Bottom)
    ) {
        ServerAddressField(
            address = serverAddress,
            onAddressChange = onServerAddressChange,
            modifier = Modifier.widthIn(max = 480.dp)
        )
        AuthFields(
            authData = authData,
            onAuthDataChange = onAuthDataChange,
            modifier = Modifier.widthIn(max = 480.dp)
        )
    }
}

@Composable
fun FindServerHorizontalContent(
    serverAddress: String,
    onServerAddressChange: (String) -> Unit,
    authData: AuthData,
    onAuthDataChange: (AuthData) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.large)
    ) {
        ServerAddressField(
            address = serverAddress,
            onAddressChange = onServerAddressChange,
            modifier = Modifier.weight(1f)
        )
        AuthFields(
            authData = authData,
            onAuthDataChange = onAuthDataChange,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun FindServerCenteredContent(
    serverAddress: String,
    onServerAddressChange: (String) -> Unit,
    authData: AuthData,
    onAuthDataChange: (AuthData) -> Unit,
    modifier: Modifier = Modifier
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
                    modifier = Modifier.widthIn(max = 480.dp)
                )
                AuthFields(
                    authData = authData,
                    onAuthDataChange = onAuthDataChange,
                    modifier = Modifier.widthIn(max = 480.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.S)
@PreviewLightDark
@PreviewScreenSizes
@Composable
fun FindServerScreenPreview() {
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
            FindServerContent(
                serverAddress = serverAddress,
                onServerAddressChange = { serverAddress = it },
                authData = authData,
                onAuthDataChange = { authData = it },
                windowSizeClass = windowSizeClass,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialThemeExt.colorScheme.background),
                contentPadding = PaddingValues(MaterialThemeExt.paddings.large)
            )
        }
    }
}
