package com.nasdroid.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Describes a single navigation item displayed in navigation-related UI.
 *
 * @property icon An [ImageVector] used to represent the nav destination.
 * @property labelRes A string resource for the destination label.
 * @property route The navigation route for which this destination belongs.
 */
data class NavigationItem(
    val icon: ImageVector,
    @StringRes val labelRes: Int,
    val route: String
)

internal val LocalBottomNavigationItems = compositionLocalOf<List<NavigationItem>> {
    emptyList()
}

internal val LocalNavigationRailItems = compositionLocalOf<List<NavigationItem>> {
    emptyList()
}

internal val LocalNavigationDrawerItems = compositionLocalOf<List<NavigationItem>> {
    emptyList()
}

/**
 * Provides all variants of [NavigationItem]s required to display navigation components. It's
 * required that this is used at least once before [NavigationDrawerLayout] and
 * [NavigationSuiteScaffold].
 */
@Composable
fun ProvideNavigationItems(
    bottomNavigationItems: List<NavigationItem>,
    navigationRailItems: List<NavigationItem>,
    navigationDrawerItems: List<NavigationItem>,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalBottomNavigationItems provides bottomNavigationItems,
        LocalNavigationRailItems provides navigationRailItems,
        LocalNavigationDrawerItems provides navigationDrawerItems,
        content = content
    )
}
