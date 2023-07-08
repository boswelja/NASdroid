package com.boswelja.truemanager.core.menuprovider

interface MenuHost {
    val menuItems: List<MenuItem>

    fun addItems(vararg newItems: MenuItem)

    fun removeItems(vararg items: MenuItem)
}
