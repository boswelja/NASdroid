package com.nasdroid.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lan
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.ui.graphics.vector.ImageVector
import com.nasdroid.R

/**
 * Describes a top-level navigation destination. These will appear in the main navigation component.
 *
 * @property icon The [ImageVector] to use as the items icon.
 * @property labelRes A string resource pointing to the items label. The label should be at most 1-2
 * words that describe the destination.
 * @property route The string route to use when navigating to the destination.
 */
enum class TopLevelDestination(
    val icon: ImageVector,
    @StringRes val labelRes: Int,
    val route: String
) {
    Dashboard(
        icon = Icons.Default.Dashboard,
        labelRes = R.string.feature_dashboard,
        route = "dashboard"
    ),
    Storage(
        icon = Icons.Default.Storage,
        labelRes = R.string.feature_storage,
        route = "storage"
    ),
    Datasets(
        icon = Icons.Default.Dataset,
        labelRes = R.string.feature_datasets,
        route = "datasets"
    ),
    Shares(
        icon = Icons.Default.FolderShared,
        labelRes = R.string.feature_shares,
        route = "shares"
    ),
    DataProtection(
        icon = Icons.Default.Security,
        labelRes = R.string.feature_data_protection,
        route = "data_protection"
    ),
    Network(
        icon = Icons.Default.Lan,
        labelRes = R.string.feature_network,
        route = "network"
    ),
    Credentials(
        icon = Icons.Default.Key,
        labelRes = R.string.feature_credentials,
        route = "credentials"
    ),
    Virtualization(
        icon = Icons.Default.Computer,
        labelRes = R.string.feature_virtualization,
        route = "virtualization"
    ),
    Apps(
        icon = Icons.Default.Apps,
        labelRes = R.string.feature_apps,
        route = "apps"
    ),
    Reporting(
        icon = Icons.Default.Analytics,
        labelRes = R.string.feature_reporting,
        route = "reporting"
    ),
    SystemSettings(
        icon = Icons.Default.Settings,
        labelRes = R.string.feature_system_settings,
        route = "system_settings"
    ),
}

/**
 * Describes a top-level navigation destination. These will appear in the main navigation component.
 *
 * @property icon The [ImageVector] to use as the items icon.
 * @property labelRes A string resource pointing to the items label. The label should be at most 1-2
 * words that describe the destination.
 */
enum class TopLevelDestination2(
    val icon: ImageVector,
    @StringRes val labelRes: Int,
) {
    Dashboard(
        icon = Icons.Default.Dashboard,
        labelRes = R.string.feature_dashboard,
    ),
    Storage(
        icon = Icons.Default.Storage,
        labelRes = R.string.feature_storage,
    ),
    Datasets(
        icon = Icons.Default.Dataset,
        labelRes = R.string.feature_datasets,
    ),
    Shares(
        icon = Icons.Default.FolderShared,
        labelRes = R.string.feature_shares,
    ),
    DataProtection(
        icon = Icons.Default.Security,
        labelRes = R.string.feature_data_protection,
    ),
    Network(
        icon = Icons.Default.Lan,
        labelRes = R.string.feature_network,
    ),
    Credentials(
        icon = Icons.Default.Key,
        labelRes = R.string.feature_credentials,
    ),
    Virtualization(
        icon = Icons.Default.Computer,
        labelRes = R.string.feature_virtualization,
    ),
    Apps(
        icon = Icons.Default.Apps,
        labelRes = R.string.feature_apps,
    ),
    Reporting(
        icon = Icons.Default.Analytics,
        labelRes = R.string.feature_reporting,
    ),
    SystemSettings(
        icon = Icons.Default.Settings,
        labelRes = R.string.feature_system_settings,
    ),
}
