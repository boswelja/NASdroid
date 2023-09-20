package com.nasdroid.storage.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nasdroid.api.v2.pool.Topology
import com.nasdroid.api.v2.pool.VDev
import com.nasdroid.storage.R

/**
 * Displays information about a given [Topology].
 */
@Composable
fun TopologyDetails(
    topology: Topology,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.topology_title),
            style = MaterialTheme.typography.titleMedium
        )
        TopologySection(
            title = { Text(stringResource(R.string.topology_section_data)) },
            vdevs = topology.data,
            modifier = Modifier.fillMaxWidth()
        )
        TopologySection(
            title = { Text(stringResource(R.string.topology_section_cache)) },
            vdevs = topology.cache,
            modifier = Modifier.fillMaxWidth()
        )
        TopologySection(
            title = { Text(stringResource(R.string.topology_section_dedup)) },
            vdevs = topology.dedup,
            modifier = Modifier.fillMaxWidth()
        )
        TopologySection(
            title = { Text(stringResource(R.string.topology_section_log)) },
            vdevs = topology.log,
            modifier = Modifier.fillMaxWidth()
        )
        TopologySection(
            title = { Text(stringResource(R.string.topology_section_spare)) },
            vdevs = topology.spare,
            modifier = Modifier.fillMaxWidth()
        )
        TopologySection(
            title = { Text(stringResource(R.string.topology_section_meta)) },
            vdevs = topology.special,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Displays a list of VDEVs for a topology category.
 */
@Composable
fun TopologySection(
    title: @Composable () -> Unit,
    vdevs: List<VDev>,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        ProvideTextStyle(MaterialTheme.typography.labelLarge) {
            title()
        }
        if (vdevs.isNotEmpty()) {
            vdevs.forEach { vdev ->
                if (vdev.children.isNotEmpty()) {
                    Text(stringResource(R.string.topology_entry_raidz, vdev.type, vdev.children.count()))
                } else {
                    Text(stringResource(R.string.topology_entry_jbod, vdev.type))
                }
            }
        } else {
            Text(stringResource(R.string.topology_entry_empty))
        }
    }
}
