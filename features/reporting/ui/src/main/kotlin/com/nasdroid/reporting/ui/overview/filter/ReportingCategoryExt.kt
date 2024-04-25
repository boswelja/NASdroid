package com.nasdroid.reporting.ui.overview.filter

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nasdroid.reporting.ui.R
import com.nasdroid.reporting.ui.overview.ReportingCategory

@Composable
internal fun ReportingCategory.label(): String {
    return when (this) {
        ReportingCategory.CPU -> stringResource(R.string.graph_type_cpu)
        ReportingCategory.DISK -> stringResource(R.string.graph_type_disk)
        ReportingCategory.MEMORY -> stringResource(R.string.graph_type_memory)
        ReportingCategory.NETWORK -> stringResource(R.string.graph_type_network)
        ReportingCategory.SYSTEM -> stringResource(R.string.graph_type_system)
        ReportingCategory.ZFS -> stringResource(R.string.graph_type_zfs)
    }
}
