package com.nasdroid.auth.ui.register.find

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.logic.Server
import com.nasdroid.auth.ui.R
import com.nasdroid.auth.ui.serverselect.SelectServerContent
import com.nasdroid.design.MaterialThemeExt
import org.koin.androidx.compose.koinViewModel

/**
 * A screen that allows the user to quickly find and connect to their TrueNAS server.
 */
@Composable
fun FindServerScreen(
    onServerFound: (address: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: FindServerViewModel = koinViewModel()
) {
    FindServerByAddress(
        onServerAddressChange = onServerFound,
        modifier = modifier.padding(contentPadding)
    )
}

@Composable
fun FindServerContent(
    onServerFound: (address: String) -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    FindServerByAddress(
        onServerAddressChange = onServerFound,
        modifier = modifier.padding(contentPadding)
    )
}

@Composable
internal fun FindServerByAddress(
    onServerAddressChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val (address, onAddressChange) = rememberSaveable { mutableStateOf("") }
    val canConnect by remember(address) { derivedStateOf { address.isNotBlank() }}

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        ServerAddressField(
            serverAddress = address,
            onServerAddressChange = onAddressChange,
            onKeyboardAction = {
                if (canConnect) onServerAddressChange(address)
            },
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onServerAddressChange(address) },
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth(),
            enabled = canConnect
        ) {
            Text(stringResource(R.string.connect_server))
        }
    }
}

@Composable
internal fun ServerAddressField(
    serverAddress: String,
    onServerAddressChange: (String) -> Unit,
    onKeyboardAction: () -> Unit,
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
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions { onKeyboardAction() },
        singleLine = true,
        enabled = enabled,
        isError = error,
        supportingText = if (error) {{
            Text(stringResource(R.string.invalid_server_address))
        }} else {
            null
        },
        modifier = modifier
    )
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
    MaterialThemeExt(
        colorScheme = if (isSystemInDarkTheme()) {
            dynamicDarkColorScheme(context)
        } else {
            dynamicLightColorScheme(context)
        }
    ) {
        FindServerContent(
            onServerFound = {},
            windowSizeClass = windowSizeClass,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialThemeExt.colorScheme.background),
            contentPadding = PaddingValues(16.dp)
        )
    }
}
