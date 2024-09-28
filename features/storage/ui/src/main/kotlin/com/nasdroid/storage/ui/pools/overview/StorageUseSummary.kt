package com.nasdroid.storage.ui.pools.overview

import android.text.format.Formatter.formatFileSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.boswelja.capacity.Capacity.Companion.gigabytes
import com.boswelja.capacity.Capacity.Companion.terabytes
import com.boswelja.capacity.CapacityUnit
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.design.NasDroidTheme
import com.nasdroid.storage.ui.R

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
    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.capacity_used_label, fileSizeString(usedBytes)),
                style = MaterialThemeExt.typography.labelLarge
            )
            Text(
                text = stringResource(R.string.capacity_available_label, fileSizeString(totalBytes - usedBytes)),
                style = MaterialThemeExt.typography.labelLarge
            )
        }
        Spacer(Modifier.height(MaterialThemeExt.paddings.small))
        LinearProgressIndicator(
            progress = { usedBytes / totalBytes.toFloat() },
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth(),
            strokeCap = StrokeCap.Round,
        )
    }
}

/**
 * Converts the given bytes into a human-readable size string.
 */
@Composable
fun fileSizeString(bytes: Long): String {
    val context = LocalContext.current
    return remember(context, bytes) {
        formatFileSize(context, bytes)
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
fun StorageUseSummaryPreview() {
    NasDroidTheme {
        Surface {
            StorageUseSummary(
                usedBytes = 350.gigabytes.toLong(CapacityUnit.BYTE),
                totalBytes = 1.terabytes.toLong(CapacityUnit.BYTE),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
