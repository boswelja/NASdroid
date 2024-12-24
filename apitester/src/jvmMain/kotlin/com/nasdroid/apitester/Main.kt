package com.nasdroid.apitester

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val state = rememberWindowState()
    Window(title = "TrueNAS API Tester", onCloseRequest = ::exitApplication, state = state) {
        App()
    }
}
