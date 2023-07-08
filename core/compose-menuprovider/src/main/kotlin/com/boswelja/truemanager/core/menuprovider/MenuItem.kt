package com.boswelja.truemanager.core.menuprovider

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Describes an item in a menu.
 *
 * @property label The label for the menu item. If it is displayed as an icon, this should be the
 * content description.
 * @property imageVector The icon for the menu item.
 * @property onClick Called when the menu item is clicked.
 * @property isImportant Whether the item should be considered "important". Important items should
 * be displayed as icons outside of an overflow menu, if possible.
 */
data class MenuItem(
    val label: String,
    val imageVector: ImageVector,
    val onClick: () -> Unit,
    val isImportant: Boolean,
)
