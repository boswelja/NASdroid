package com.nasdroid.apps.ui.installed.overview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.boswelja.menuprovider.MenuItem
import com.boswelja.menuprovider.ProvideMenuItems
import com.nasdroid.apps.ui.R
import com.nasdroid.apps.ui.installed.overview.item.ApplicationOverviewItem
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.milliseconds

/**
 * A screen for displaying apps installed on the system.
 */
@Composable
fun InstalledAppsOverviewScreen(
    onAppClick: (appName: String) -> Unit,
    onNavigate: (route: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: InstalledAppsOverviewViewModel = koinViewModel()
) {
    ProvideMenuItems(
        MenuItem(
            label = stringResource(R.string.menu_item_refresh),
            imageVector = Icons.Default.Refresh,
            onClick = viewModel::refresh,
            isImportant = true
        )
    )

    val installedApps by viewModel.installedApps.collectAsState()
    val searchTerm by viewModel.searchTerm.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(modifier) {
        LoadingIndicator(
            visible = isLoading,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        LazyColumn(
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                SearchField(
                    value = searchTerm,
                    onValueChange = viewModel::setSearchTerm,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            installedApps?.let { apps ->
                items(
                    items = apps,
                    key = { it.name }
                ) { applicationOverview ->
                    ApplicationOverviewItem(
                        installedAppOverview = applicationOverview,
                        onClick = { onAppClick(applicationOverview.name) },
                        onAppStartRequest = { viewModel.start(applicationOverview.name) },
                        onAppStopRequest = { viewModel.stop(applicationOverview.name) },
                    )
                }
            }
            item {
                Spacer(Modifier.height(56.dp)) // TODO Don't use Spacer for extended FAB padding
            }
        }

        ExtendedFloatingActionButton(
            onClick = { onNavigate("discover") },
            modifier = Modifier
                .padding(contentPadding)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Default.GetApp, contentDescription = null)
            Text("Discover Apps")
        }
    }
}

@Composable
internal fun LoadingIndicator(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
internal fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchQuery by rememberSaveable(value) {
        mutableStateOf(value)
    }
    LaunchedEffect(key1 = searchQuery) {
        // Debounce submitting updates
        delay(100.milliseconds)
        onValueChange(searchQuery)
    }
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = {
            Text(stringResource(R.string.overview_app_search))
        },
        leadingIcon = {
            Icon(Icons.Default.Search, null)
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { searchQuery = "" }) {
                    Icon(Icons.Default.Clear, stringResource(R.string.overview_app_clear_search))
                }
            }
        },
        keyboardActions = KeyboardActions {
            onValueChange(searchQuery)
        },
        singleLine = true,
        modifier = modifier
    )
}
