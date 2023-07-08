package com.boswelja.truemanager.core.menuprovider

import androidx.compose.runtime.compositionLocalOf

/**
 * A CompositionLocal that holds a [MenuHost]. THis provides convenient access from any
 * Composable that has a parent providing a menu.
 */
val LocalMenuHost = compositionLocalOf<MenuHost> { error("No MenuProvider found!") }
