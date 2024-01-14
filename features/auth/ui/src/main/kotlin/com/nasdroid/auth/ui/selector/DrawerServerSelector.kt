package com.nasdroid.auth.ui.selector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.design.NasDroidTheme
import org.koin.androidx.compose.koinViewModel

/**
 * An authenticated server selector designed to fit inside a navigation drawer as a header.
 */
@Composable
fun DrawerServerSelector(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ServerSelectorViewModel = koinViewModel()
) {
    val selectedServer by viewModel.selectedServer.collectAsState()
    DrawerServerSelector(
        serverAddress = selectedServer.url,
        serverName = selectedServer.name,
        onLogoutClick = {
            viewModel.logOut()
            onLogout()
        },
        modifier = modifier,
    )
}

@Composable
internal fun DrawerServerSelector(
    serverName: String,
    serverAddress: String,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalContentColor provides MaterialThemeExt.colorScheme.onSurfaceVariant) {
        Row(
            modifier = modifier.aspectRatio(2.2f),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Connected to",
                    style = MaterialThemeExt.typography.headlineSmall,
                )
                Text(
                    text = serverName,
                    style = MaterialThemeExt.typography.displaySmall,
                )
                Text(
                    text = serverAddress,
                    style = MaterialThemeExt.typography.titleMedium,
                )
            }
            Column {
                OutlinedIconButton(onClick = onLogoutClick) {
                    Icon(Icons.AutoMirrored.Default.Logout, contentDescription = "Log out")
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DrawerAccountSelectorPreview() {
    NasDroidTheme {
        DrawerServerSelector(
            serverName = "Server name",
            serverAddress = "http://truenas.local",
            onLogoutClick = { /* no-op */ },
            modifier = Modifier
                .width(360.dp)
                .padding(16.dp)
        )
    }
}