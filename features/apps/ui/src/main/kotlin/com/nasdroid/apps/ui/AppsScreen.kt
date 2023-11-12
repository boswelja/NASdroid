package com.nasdroid.apps.ui

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nasdroid.apps.ui.available.AvailableAppsScreen
import com.nasdroid.apps.ui.installed.InstalledAppsScreen

/**
 * The main entrypoint for the Apps feature. This screen is tabbed to allow the user to switch
 * between installed and available apps, as well as catalog and cache management.
 */
@Composable
fun AppsScreen(
    onShowLogs: (appName: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(modifier) {
        val (selectedTab, setSelectedTab) = remember { mutableStateOf(TabItem.INSTALLED_APPS) }
        AppsTabRow(
            selectedTab = selectedTab,
            onSelectTabItem = setSelectedTab,
            modifier = Modifier.fillMaxWidth()
        )
        AnimatedContent(targetState = selectedTab, label = "Selected tab content") { tab ->
            when (tab) {
                TabItem.INSTALLED_APPS -> {
                    InstalledAppsScreen(
                        onShowLogs = onShowLogs,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = contentPadding,
                    )
                }
                TabItem.AVAILABLE_APPS -> {
                    AvailableAppsScreen(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = contentPadding,
                    )
                }
                TabItem.MANAGE_CATALOGS -> {
                    TODO()
                }
                TabItem.MANAGE_DOCKER_IMAGES -> {
                    TODO()
                }
            }
        }
    }
}

@Composable
internal fun AppsTabRow(
    selectedTab: TabItem,
    onSelectTabItem: (TabItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedIndex by remember(selectedTab) {
        derivedStateOf { TabItem.entries.indexOf(selectedTab) }
    }
    PrimaryTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier
    ) {
        TabItem.entries.forEach {
            Tab(
                selected = it == selectedTab,
                onClick = { onSelectTabItem(it) },
                text = { Text(stringResource(it.labelRes)) },
                icon = { Icon(imageVector = it.icon, contentDescription = null) }
            )
        }
    }
}

/**
 * Contains all possible tabs shown in the apps screen.
 *
 * @property labelRes The string resource of the tab label.
 * @property icon The tab icon.
 */
enum class TabItem(@StringRes val labelRes: Int, val icon: ImageVector) {
    INSTALLED_APPS(R.string.tab_installed_apps, icon = Icons.Default.Apps),
    AVAILABLE_APPS(R.string.tab_available_apps, icon = Icons.Default.AppRegistration),
    MANAGE_CATALOGS(R.string.tab_manage_catalogs, icon = Icons.AutoMirrored.Filled.ListAlt),
    MANAGE_DOCKER_IMAGES(R.string.tab_manage_docker, icon = Icons.Default.Inventory),
}

@Preview
@Composable
fun AppsTabRowPreview() {
    val (selectedTab, setSelectedTab) = remember { mutableStateOf(TabItem.INSTALLED_APPS) }
    AppsTabRow(selectedTab = selectedTab, onSelectTabItem = setSelectedTab)
}
