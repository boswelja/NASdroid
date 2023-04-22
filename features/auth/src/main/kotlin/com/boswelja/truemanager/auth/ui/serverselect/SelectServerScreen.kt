package com.boswelja.truemanager.auth.ui.serverselect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.auth.ui.common.AuthHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun SelectServerScreen(
    onLoginSuccess: () -> Unit,
    onAddServer: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: SelectServerViewModel = koinViewModel()
) {
    val layoutDirection = LocalLayoutDirection.current
    val authenticatedServers by viewModel.authenticatedServers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(modifier) {
        AuthHeader(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .padding(contentPadding)
        )
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding())
        ) {
            items(
                items = authenticatedServers,
                key = { it.token }
            ) { authenticatedServer ->
                ListItem(
                    headlineContent = { Text(authenticatedServer.name) },
                    supportingContent = { Text(authenticatedServer.serverAddress) },
                    leadingContent = { Icon(Icons.Default.Dns, contentDescription = null) },
                    modifier = Modifier
                        .clickable(enabled = !isLoading) { viewModel.tryLogIn(authenticatedServer) }
                        .padding(
                            start = contentPadding.calculateStartPadding(layoutDirection),
                            end = contentPadding.calculateEndPadding(layoutDirection)
                        )
                )
            }
            item {
                ListItem(
                    headlineContent = { Text("Add Server") },
                    leadingContent = { Icon(Icons.Default.Add, contentDescription = null) },
                    modifier = Modifier
                        .clickable(onClick = onAddServer, enabled = !isLoading)
                        .padding(
                            start = contentPadding.calculateStartPadding(layoutDirection),
                            end = contentPadding.calculateEndPadding(layoutDirection)
                        )
                )
            }
        }
    }
}
