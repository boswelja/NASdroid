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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.nasdroid.design.MaterialThemeExt

/**
 * A screen that allows the user to quickly find and connect to their TrueNAS server.
 */
@Composable
fun RegisterServerScreen(
    onServerFound: (address: String) -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    FindServerContent(
        onServerFound = onServerFound,
        windowSizeClass = windowSizeClass,
        modifier = modifier,
        contentPadding = contentPadding
    )
}

@Composable
fun FindServerContent(
    onServerFound: (address: String) -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    when {
        windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact &&
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact -> {
            FindServerHorizontalContent(
                onServerAddressChange = onServerFound,
                modifier = modifier.padding(contentPadding)
            )
        }
        windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact &&
                windowSizeClass.heightSizeClass > WindowHeightSizeClass.Compact -> {
            FindServerCenteredContent(
                onServerAddressChange = onServerFound,
                modifier = modifier.padding(contentPadding)
            )
        }
        else -> {
            FindServerVerticalContent(
                onServerAddressChange = onServerFound,
                modifier = modifier.padding(contentPadding)
            )
        }
    }
}

@Composable
fun FindServerVerticalContent(
    onServerAddressChange: (address: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.large, Alignment.Bottom)
    ) {
        ServerAddressField(
            address = "",
            onAddressChange = onServerAddressChange,
            modifier = Modifier.widthIn(max = 480.dp)
        )
        AuthFields(
            authData = AuthData.ApiKey(""),
            onAuthDataChange = {},
            modifier = Modifier.widthIn(max = 480.dp)
        )
    }
}

@Composable
fun FindServerHorizontalContent(
    onServerAddressChange: (address: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.large)
    ) {
        ServerAddressField(
            address = "",
            onAddressChange = onServerAddressChange,
            modifier = Modifier.weight(1f)
        )
        AuthFields(
            authData = AuthData.ApiKey(""),
            onAuthDataChange = {},
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun FindServerCenteredContent(
    onServerAddressChange: (address: String) -> Unit,
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
                    address = "",
                    onAddressChange = onServerAddressChange,
                    modifier = Modifier.widthIn(max = 480.dp)
                )
                AuthFields(
                    authData = AuthData.ApiKey(""),
                    onAuthDataChange = {},
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
    MaterialThemeExt(
        colorScheme = if (isSystemInDarkTheme()) {
            dynamicDarkColorScheme(context)
        } else {
            dynamicLightColorScheme(context)
        }
    ) {
        Surface {
            FindServerContent(
                onServerFound = {},
                windowSizeClass = windowSizeClass,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialThemeExt.colorScheme.background),
                contentPadding = PaddingValues(MaterialThemeExt.paddings.large)
            )
        }
    }
}
