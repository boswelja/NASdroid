package com.boswelja.truemanager.auth.ui.serverselect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
    val authenticatedServers by viewModel.authenticatedServers.collectAsState()

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding())
    ) {
        item {
            AuthHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
                    .padding(contentPadding)
            )
        }
        items(
            items = authenticatedServers,
            key = { it.token }
        ) { authenticatedServer ->
            ListItem(headlineContent = { Text(authenticatedServer.serverAddress) })
        }
        item {
            ListItem(
                headlineContent = { Text("Add Server") },
                modifier = Modifier.clickable(onClick = onAddServer)
            )
        }
    }
}
