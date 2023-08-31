package com.boswelja.truemanager.auth.ui.serverselect

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.auth.logic.manageservers.Server
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SelectServerScreen(
    onLoginSuccess: () -> Unit,
    onAddServer: () -> Unit,
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
        modifier = modifier,
        contentPadding = contentPadding,
    )
}

@Composable
fun SelectServerContent(
    isLoading: Boolean,
    servers: List<Server>,
    onServerClick: (Server) -> Unit,
    onAddServerClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val layoutDirection = LocalLayoutDirection.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthHeader(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .heightIn(min = 120.dp)
                .padding(contentPadding)
        )
        ElevatedCard(
            modifier = Modifier
                .padding(contentPadding)
                .widthIn(max = 480.dp)
        ) {
            AnimatedVisibility(visible = isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.padding(
                        start = contentPadding.calculateStartPadding(layoutDirection),
                        end = contentPadding.calculateEndPadding(layoutDirection)
                    )
                )
            }
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = servers,
                    key = { it.id }
                ) { authenticatedServer ->
                    ListItem(
                        headlineContent = { Text(authenticatedServer.name) },
                        supportingContent = { Text(authenticatedServer.url) },
                        leadingContent = { Icon(Icons.Default.Dns, contentDescription = null) },
                        modifier = Modifier
                            .clickable(enabled = !isLoading) { onServerClick(authenticatedServer) }
                            .padding(horizontal = 16.dp)
                    )
                }
                item {
                    ListItem(
                        headlineContent = { Text("Add Server") },
                        leadingContent = { Icon(Icons.Default.Add, contentDescription = null) },
                        modifier = Modifier
                            .clickable(onClick = onAddServerClick, enabled = !isLoading)
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@PreviewLightDark
@PreviewScreenSizes
@Composable
fun SelectServerScreenPreview() {
    val context = LocalContext.current
    MaterialTheme(
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
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp)
        )
    }
}
