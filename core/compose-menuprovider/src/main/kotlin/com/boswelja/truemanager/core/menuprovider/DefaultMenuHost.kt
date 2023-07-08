package com.boswelja.truemanager.core.menuprovider

import androidx.compose.runtime.mutableStateListOf

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
