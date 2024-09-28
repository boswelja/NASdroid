package com.nasdroid.storage.ui.pools.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.boswelja.capacity.Capacity.Companion.terabytes
import com.boswelja.capacity.CapacityUnit
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.design.NasDroidTheme
import com.nasdroid.storage.logic.pool.PoolDetails
import com.nasdroid.storage.ui.R
import com.nasdroid.storage.ui.pools.overview.fileSizeString

@Composable
fun UsageDetails(
    usage: PoolDetails.Usage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.small)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = stringResource(R.string.capacity_used_label, fileSizeString(usage.usedCapacity.toLong(CapacityUnit.BYTE))),
                style = MaterialThemeExt.typography.labelLarge,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(0.5f)
            )
            Text(
                text = stringResource(R.string.capacity_available_label, fileSizeString(usage.availableCapacity.toLong(CapacityUnit.BYTE))),
                style = MaterialThemeExt.typography.labelLarge,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
        LinearProgressIndicator(
            progress = { usage.usedCapacity.toLong(CapacityUnit.BYTE) / usage.usableCapacity.toLong(CapacityUnit.BYTE).toFloat() },
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth(),
            strokeCap = StrokeCap.Round,
        )
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
fun UsageDetailsPreview() {
    NasDroidTheme {
        Surface {
            UsageDetails(
                usage = PoolDetails.Usage(
                    usableCapacity = 100.terabytes,
                    usedCapacity = 50.terabytes,
                    availableCapacity = 50.terabytes
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
