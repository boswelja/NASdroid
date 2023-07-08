package com.boswelja.truemanager.core.menuprovider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

internal class DefaultMenuHost : MenuHost {
    private val _menuItems = mutableStateListOf<MenuItem>()

    override val menuItems: List<MenuItem> = _menuItems

    override fun addItems(vararg newItems: MenuItem) {
        _menuItems.addAll(newItems)
    }

    override fun removeItems(vararg items: MenuItem) {
        _menuItems.removeAll(items.toSet())
    }
}

/**
 * Creates a new [MenuHost] for use in Composition. This should be provided via [LocalMenuHost].
 */
@Composable
fun rememberMenuHost(): MenuHost {
    return remember {
        DefaultMenuHost()
    }
}
