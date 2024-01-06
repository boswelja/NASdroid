package com.nasdroid.auth.ui.serverselect

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.logic.Server
import com.nasdroid.design.MaterialThemeExt
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

/**
 * A screen that allows users to select a registered server to log in to, or add a new server.
 */
@Composable
fun SelectServerScreen(
    onLoginSuccess: () -> Unit,
    onAddServer: () -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: SelectServerViewModel = koinViewModel()
) {
    val authenticatedServers by viewModel.authenticatedServers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest {
            when (it) {
                SelectServerViewModel.Event.LoginSuccess -> onLoginSuccess()
                SelectServerViewModel.Event.LoginFailedTokenInvalid -> TODO()
                SelectServerViewModel.Event.LoginFailedServerNotFound -> TODO()
                null -> return@collectLatest
            }
            viewModel.clearPendingEvent()
        }
    }

    SelectServerContent(
        isLoading = isLoading,
        servers = authenticatedServers,
        onServerClick = viewModel::tryLogIn,
        onAddServerClick = onAddServer,
        windowSizeClass = windowSizeClass,
        modifier = modifier,
        contentPadding = contentPadding,
    )
}

/**
 * Size-aware content for [SelectServerScreen]. This will choose the appropriate layout for the
 * given [WindowSizeClass].
 */
@Composable
fun SelectServerContent(
    isLoading: Boolean,
    servers: List<Server>,
    onServerClick: (Server) -> Unit,
    onAddServerClick: () -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    when {
        windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact &&
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact -> {
            SelectServerHorizontalContent(
                isLoading = isLoading,
                servers = servers,
                onServerClick = onServerClick,
                onAddServerClick = onAddServerClick,
                modifier = modifier,
                contentPadding = contentPadding,
            )
        }
        windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded &&
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Expanded -> {
            SelectServerCenteredContent(
                isLoading = isLoading,
                servers = servers,
                onServerClick = onServerClick,
                onAddServerClick = onAddServerClick,
                modifier = modifier,
                contentPadding = contentPadding,
            )
        }
        else -> {
            SelectServerVerticalContent(
                isLoading = isLoading,
                servers = servers,
                onServerClick = onServerClick,
                onAddServerClick = onAddServerClick,
                modifier = modifier,
                contentPadding = contentPadding,
            )
        }
    }
}

/**
 * Content for [SelectServerScreen], where the content is laid out vertically. The server picker is
 * aligned to the bottom of the screen, and app branding is centered in the remaining space.
 */
@Composable
fun SelectServerVerticalContent(
    isLoading: Boolean,
    servers: List<Server>,
    onServerClick: (Server) -> Unit,
    onAddServerClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBranding(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 120.dp)
                .padding(contentPadding)
        )
        ServerSelectorCard(
            servers = servers,
            loading = isLoading,
            onServerClick = onServerClick,
            onAddServerClick = onAddServerClick,
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

/**
 * Content for [SelectServerScreen], where the content is laid out horizontally. The server picker
 * is constrained to the end half of the screen, while app branding is centered in the start half.
 */
@Composable
fun SelectServerHorizontalContent(
    isLoading: Boolean,
    servers: List<Server>,
    onServerClick: (Server) -> Unit,
    onAddServerClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppBranding(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 120.dp)
                .padding(contentPadding)
        )
        ServerSelectorCard(
            servers = servers,
            loading = isLoading,
            onServerClick = onServerClick,
            onAddServerClick = onAddServerClick,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        )
    }
}

/**
 * Content for [SelectServerScreen], where the content is centered in the screen. The server picker
 * is laid out below app branding, and can grow or shrink as necessary.
 */
@Composable
fun SelectServerCenteredContent(
    isLoading: Boolean,
    servers: List<Server>,
    onServerClick: (Server) -> Unit,
    onAddServerClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AppBranding(
            modifier = Modifier
                .height(180.dp)
                .padding(contentPadding)
        )
        ServerSelectorCard(
            servers = servers,
            loading = isLoading,
            onServerClick = onServerClick,
            onAddServerClick = onAddServerClick,
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .padding(32.dp)
        )
    }
}

/**
 * A Card that allows the user to select a server, or start the "add server" flow.
 */
@Composable
fun ServerSelectorCard(
    servers: List<Server>,
    loading: Boolean,
    onServerClick: (Server) -> Unit,
    onAddServerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier
    ) {
        AnimatedVisibility(visible = loading) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
        RegisteredServerList(
            servers = servers,
            enabled = !loading,
            onServerClick = onServerClick,
            onAddServerClick = onAddServerClick,
            contentPadding = PaddingValues(vertical = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.S)
@PreviewLightDark
@PreviewScreenSizes
@Composable
fun SelectServerScreenPreview() {
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
        SelectServerContent(
            isLoading = false,
            servers = listOf(
                Server(
                    name = "Server 1",
                    url = "http://my.server",
                    id = "0"
                ),
                Server(
                    name = "Server 2",
                    url = "http://my.other.server",
                    id = "1"
                )
            ),
            onServerClick = { },
            onAddServerClick = { },
            windowSizeClass = windowSizeClass,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialThemeExt.colorScheme.background),
            contentPadding = PaddingValues(16.dp)
        )
    }
}
