package com.nasdroid.auth.ui.register.find

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import com.nasdroid.design.MaterialThemeExt

@Composable
fun FindServerByCommonHostname(
    onServerSelected: (address: String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(modifier) {
        Text(
            text = "Connect to a server via the default address",
            style = MaterialThemeExt.typography.titleMedium
        )
        DEFAULT_KNOWN_ADDRESSES.forEach { address ->
            ListItem(
                headlineContent = { Text(address) },
                trailingContent = { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null) },
                leadingContent = { Icon(Icons.Default.Dns, contentDescription = null) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier.clickable(enabled = enabled, role = Role.Button) { onServerSelected(address) }
            )
        }
    }
}

internal val DEFAULT_KNOWN_ADDRESSES = listOf(
    "http://truenas.local/"
)

@PreviewFontScale
@Composable
fun FindServerByCommonHostnamePreview() {
    MaterialThemeExt {
        Surface {
            FindServerByCommonHostname(onServerSelected = {}, modifier = Modifier.padding(16.dp))
        }
    }
}
