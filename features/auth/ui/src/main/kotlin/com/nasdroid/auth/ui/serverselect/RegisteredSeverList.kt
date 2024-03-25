package com.nasdroid.auth.ui.serverselect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.logic.Server
import com.nasdroid.auth.ui.R
import com.nasdroid.design.MaterialThemeExt

// TODO Move this somewhere more accessible to everything else
internal const val DISABLED_ALPHA = 0.38f

/**
 * Displays a list of "registered" servers, and an "add" button to add new servers.
 *
 * @param servers The list of [Server]s that are "registered".
 * @param enabled Whether items in the list can be interacted with.
 * @param onServerClick Called when a server is clicked.
 * @param onAddServerClick Called when the "add" button is clicked.
 * @param modifier [Modifier].
 * @param contentPadding The [PaddingValues] for content.
 */
@Composable
fun RegisteredServerList(
    servers: List<Server>,
    enabled: Boolean,
    onServerClick: (Server) -> Unit,
    onAddServerClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
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
                    .clickable(enabled = enabled, role = Role.Button) {
                        onServerClick(authenticatedServer)
                    }
                    .padding(horizontal = 16.dp)
                    .alpha(if (enabled) DefaultAlpha else DISABLED_ALPHA),
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
        }
        item {
            ListItem(
                headlineContent = { Text(stringResource(R.string.add_server_button)) },
                leadingContent = { Icon(Icons.Default.Add, contentDescription = null) },
                modifier = Modifier
                    .clickable(onClick = onAddServerClick, enabled = enabled, role = Role.Button)
                    .padding(horizontal = 16.dp)
                    .alpha(if (enabled) DefaultAlpha else DISABLED_ALPHA),
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
        }
    }
}

@PreviewFontScale
@PreviewLightDark
@Composable
fun RegisteredServerListPreview() {
    MaterialThemeExt(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        RegisteredServerList(
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
            enabled = true,
        )
    }
}
