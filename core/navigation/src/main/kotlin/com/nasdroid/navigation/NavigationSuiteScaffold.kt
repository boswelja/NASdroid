package com.nasdroid.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.boswelja.menuprovider.MenuHost
import com.boswelja.menuprovider.ProvideMenuHost
import com.boswelja.menuprovider.material3.AnimatedTopAppBarMenuItems
import com.boswelja.menuprovider.rememberCumulativeMenuHost
import com.nasdroid.navigation.navbar.BottomNavBar
import com.nasdroid.navigation.navrail.StartNavRail
import com.nasdroid.navigation.topbar.NavigableTopAppBar

/**
 * An opinionated Scaffold that determines which mode of navigation is displayed based on
 * [navigationMode]. Make sure to call [ProvideNavigationItems] before use!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationSuiteScaffold(
    title: @Composable () -> Unit,
    onNavigate: (route: String) -> Unit,
    modifier: Modifier = Modifier,
    navigationMode: NavigationMode = LocalNavigationMode.current,
    selectedItem: NavigationItem? = LocalSelectedNavigationItem.current,
    navigationBarItems: List<NavigationItem> = LocalBottomNavigationItems.current,
    navigationRailItems: List<NavigationItem> = LocalNavigationRailItems.current,
    menuHost: MenuHost = rememberCumulativeMenuHost(),
    content: @Composable (PaddingValues) -> Unit
) {
    val contentWithProviders = @Composable { paddingValues: PaddingValues ->
        ProvideMenuHost(menuHost) {
            content(paddingValues)
        }
    }
    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    if (navigationMode.secondaryNavigationMode == SecondaryNavigationMode.Rail) {
        Row(modifier) {
            StartNavRail(
                items = navigationRailItems,
                selectedItem = selectedItem,
                onItemClick = { onNavigate(it.route) }
            )
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = title,
                        actions = { AnimatedTopAppBarMenuItems(menuHost = menuHost) },
                        scrollBehavior = topBarScrollBehavior
                    )
                },
                modifier = Modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection),
                content = contentWithProviders
            )
        }
    } else {
        Scaffold(
            bottomBar = {
                if (navigationMode.secondaryNavigationMode == SecondaryNavigationMode.Bar) {
                    BottomNavBar(
                        items = navigationBarItems,
                        selectedItem = selectedItem,
                        onItemClick = { onNavigate(it.route) }
                    )
                }
            },
            topBar = {
                if (navigationMode.primaryNavigationMode == PrimaryNavigationMode.Permanent) {
                    // A permanent navigation drawer cannot be opened or closed, so we don't show the
                    // option
                    TopAppBar(
                        title = title,
                        actions = { AnimatedTopAppBarMenuItems(menuHost = menuHost) },
                        scrollBehavior = topBarScrollBehavior
                    )
                } else {
                    NavigableTopAppBar(title = title, menuHost = menuHost, scrollBehavior = topBarScrollBehavior)
                }
            },
            modifier = modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection),
            content = contentWithProviders
        )
    }
}

@Composable
internal fun NavigationRailItem(
    item: NavigationItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.NavigationRailItem(
        selected = selected,
        onClick = onClick,
        icon = { Icon(item.icon, contentDescription = null) },
        label = { Text(stringResource(item.labelRes)) },
        modifier = modifier
    )
}

@Composable
internal fun RowScope.BottomNavigationItem(
    item: NavigationItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = { Icon(imageVector = item.icon, contentDescription = null) },
        label = {
            Text(
                text = stringResource(item.labelRes),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        alwaysShowLabel = true,
        modifier = modifier
    )
}
