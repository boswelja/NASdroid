package com.boswelja.truemanager.storage.overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.core.api.v2.pool.Scan
import com.boswelja.truemanager.storage.R
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun ScanDetails(
    scan: Scan,
    modifier: Modifier = Modifier
) {
    // If we have no end time, the scan is in progress.
    if (scan.endTime == null) {
        val scanProgress by remember {
            derivedStateOf {
                scan.bytesProcessed / scan.bytesToProcess.toFloat()
            }
        }
        ThreeLineListItem(
            leadingContent = {
                val contentDescription = stringResource(R.string.scan_progress_percent, scanProgress)
                CircularProgressIndicator(
                    progress = scanProgress,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.semantics { this.contentDescription = contentDescription }
                )
            },
            overlineText = {
                Text(stringResource(R.string.scan_started_on, scan.startTime.format()))
            },
            headlineText = {
                Text(stringResource(R.string.scan_in_progress, scan.function))
            },
            supportingText = {
                Text(stringResource(R.string.scan_time_remaining, scan.remainingTime))
            },
            modifier = modifier
        )
    } else {
        ThreeLineListItem(
            leadingContent = {
                if (scan.errors > 0) {
                    Icon(Icons.Default.Error, contentDescription = null)
                } else {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                }
            },
            overlineText = {
                Text(stringResource(R.string.scan_finished_on, scan.endTime!!.format()))
            },
            headlineText = {
                Text(stringResource(R.string.scan_finished, scan.function))
            },
            supportingText = {
                Text(stringResource(R.string.scan_errors_found, scan.errors))
            },
            modifier = modifier
        )
    }
}

@Composable
fun ThreeLineListItem(
    leadingContent: @Composable BoxScope.() -> Unit,
    overlineText: @Composable () -> Unit,
    headlineText: @Composable () -> Unit,
    supportingText: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .semantics(mergeDescendants = true) {}
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .padding(end = 16.dp, top = 2.dp)
                .size(24.dp),
            content = leadingContent
        )
        Column {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.labelSmall,
                LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
                content = overlineText
            )
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyLarge,
                content = headlineText
            )
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyMedium,
                LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
                content = supportingText
            )
        }
    }
}

@Composable
fun LocalDateTime.format(): String {
    val formatter = remember { DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM) }
    return remember { toJavaLocalDateTime().format(formatter) }
}
