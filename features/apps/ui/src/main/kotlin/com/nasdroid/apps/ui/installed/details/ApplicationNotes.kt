package com.nasdroid.apps.ui.installed.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.design.MaterialThemeExt

/**
 * Displays Markdown-formatted notes about an installed application.
 */
@Composable
fun ApplicationNotes(
    note: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Notes",
            style = MaterialThemeExt.typography.headlineMedium
        )
        Spacer(Modifier.height(8.dp))
        Text(note)
    }
}

@Preview(showBackground = true)
@Composable
fun ApplicationNotesPreview() {
    MaterialThemeExt {
        ApplicationNotes(
            note = "\n# Welcome to TrueNAS SCALE\n" +
                    "Thank you for installing AdGuard Home App.\n\n\n" +
                    "# Documentation\n" +
                    "Documentation for this app can be found at https://www.truenas.com/docs.\n" +
                    "# Bug reports\n" +
                    "If you find a bug in this app, please file an issue at https://ixsystems.atlassian.net\n\n",
            modifier = Modifier.padding(16.dp)
        )
    }
}
