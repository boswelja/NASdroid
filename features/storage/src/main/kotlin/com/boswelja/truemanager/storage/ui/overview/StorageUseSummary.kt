package com.boswelja.truemanager.storage.ui.overview

import android.text.format.Formatter.formatFileSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Displays a labelled progress bar communicating an overview of storage capacity.
 *
 * @param usedBytes The number of bytes currently allocated to the storage.
 * @param totalBytes The total available bytes in the storage.
 * @param modifier [Modifier].
 */
@Composable
fun StorageUseSummary(
    usedBytes: Long,
    totalBytes: Long,
    modifier: Modifier = Modifier,
) {
    val progress by remember {
        derivedStateOf {
            usedBytes / totalBytes.toFloat()
        }
    }
    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${fileSizeString(bytes = usedBytes)} Used",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "${fileSizeString(bytes = totalBytes)} Total",
                style = MaterialTheme.typography.labelLarge
            )
        }
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth(),
            progress = progress,
            strokeCap = StrokeCap.Round
        )
    }
}

/**
 * Converts the given bytes into a human-readable size string.
 */
@Composable
fun fileSizeString(bytes: Long): String {
    val context = LocalContext.current
    return remember {
        formatFileSize(context, bytes)
    }
}
