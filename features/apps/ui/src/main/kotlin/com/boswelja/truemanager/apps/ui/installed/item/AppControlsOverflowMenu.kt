package com.boswelja.truemanager.apps.ui.installed.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun AppControlsOverflowMenu(
    canUpgrade: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        var expanded by remember { mutableStateOf(false) }
        IconButton(onClick = { expanded = true }, modifier = modifier) {
            Icon(Icons.Default.MoreVert, null)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Upgrade") },
                leadingIcon = { Icon(Icons.Default.Upgrade, null) },
                onClick = { /*TODO*/ },
                enabled = canUpgrade
            )
            DropdownMenuItem(
                text = { Text("Roll Back") },
                leadingIcon = { Icon(Icons.Default.Restore, null) },
                onClick = { /*TODO*/ }
            )
            DropdownMenuItem(
                text = { Text("Edit") },
                leadingIcon = { Icon(Icons.Default.Edit, null) },
                onClick = { /*TODO*/ }
            )
            DropdownMenuItem(
                text = { Text("Shell") },
                leadingIcon = { Icon(Icons.Default.Terminal, null) },
                onClick = { /*TODO*/ }
            )
            DropdownMenuItem(
                text = { Text("Logs") },
                leadingIcon = { Icon(Icons.Default.TextSnippet, null) },
                onClick = { /*TODO*/ }
            )
            DropdownMenuItem(
                text = { Text("Delete") },
                leadingIcon = { Icon(Icons.Default.Delete, null) },
                onClick = { /*TODO*/ }
            )
        }
    }
}
