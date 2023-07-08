package com.boswelja.truemanager.core.menuprovider

/**
 * A state holder object designed to store information about the contents of a menu, and allows
 * adding or removing menu items.
 */
interface MenuHost {

    /**
     * Items that should be displayed in the menu.
     */
    val menuItems: List<MenuItem>

    /**
     * Add items to [menuItems], triggering recomposition of menus that reference it.
     */
    fun addItems(vararg newItems: MenuItem)

    /**
     * Remove items from [menuItems], triggering recomposition of menus that reference it.
     */
    fun removeItems(vararg items: MenuItem)
}
