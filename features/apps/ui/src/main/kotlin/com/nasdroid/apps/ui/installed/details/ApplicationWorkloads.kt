package com.nasdroid.apps.ui.installed.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nasdroid.design.MaterialThemeExt

/**
 * Displays information about the workload running on this system that powers the application.
 */
@Composable
fun ApplicationWorkloads(
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = "Workloads",
            style = MaterialThemeExt.typography.headlineMedium
        )
        Spacer(Modifier.height(MaterialThemeExt.paddings.medium))
        Text("Coming soon 😉")
    }
}
