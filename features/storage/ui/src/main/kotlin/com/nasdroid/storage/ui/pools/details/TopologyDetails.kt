package com.nasdroid.storage.ui.pools.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.boswelja.capacity.Capacity.Companion.gigabytes
import com.boswelja.capacity.CapacityUnit
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.design.NasDroidTheme
import com.nasdroid.storage.logic.pool.PoolDetails

@Composable
fun TopologyDetails(
    topology: PoolDetails.Topology,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.medium)
    ) {
        VdevEntry("Data VDEVs", topology.dataTopology, modifier = Modifier.fillMaxWidth())
        VdevEntry("Metadata VDEVs", topology.metadataTopology, modifier = Modifier.fillMaxWidth())
        VdevEntry("Log VDEVs", topology.logTopology, modifier = Modifier.fillMaxWidth())
        VdevEntry("Cache VDEVs", topology.cacheTopology, modifier = Modifier.fillMaxWidth())
        VdevEntry("Spare VDEVs", topology.spareTopology, modifier = Modifier.fillMaxWidth())
        VdevEntry("Dedup VDEVs", topology.dedupTopology, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
internal fun VdevEntry(
    label: String,
    vdev: PoolDetails.Topology.TopologyDescriptor?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Spacer(Modifier.width(MaterialThemeExt.paddings.medium))
        if (vdev != null) {
            Text("${vdev.deviceCount}x ${vdev.type} | ${vdev.driveCount} wide | ${vdev.totalCapacity.toLong(CapacityUnit.GIGABYTE)} GiB")
        } else {
            Text(text = "VDEVs not assigned")
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
fun TopologyDetailsPreview() {
    NasDroidTheme {
        Surface {
            TopologyDetails(
                topology = PoolDetails.Topology(
                    dataTopology = PoolDetails.Topology.TopologyDescriptor(1, PoolDetails.Topology.TopologyDescriptor.Type.RAIDZ1, 3, 3072.gigabytes),
                    metadataTopology = PoolDetails.Topology.TopologyDescriptor(1, PoolDetails.Topology.TopologyDescriptor.Type.RAIDZ1, 3, 3072.gigabytes),
                    logTopology = null,
                    cacheTopology = null,
                    spareTopology = null,
                    dedupTopology = null
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
