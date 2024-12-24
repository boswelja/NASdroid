package com.nasdroid.apitester

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun App() {
    MaterialTheme {
        Scaffold {
            Text("Hello, world!", Modifier.padding(it))
        }
    }
}
