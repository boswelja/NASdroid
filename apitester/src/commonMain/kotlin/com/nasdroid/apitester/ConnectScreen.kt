package com.nasdroid.apitester

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectScreen(
    onConnect: (url: String, session: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var url by rememberSaveable { mutableStateOf("") }
    var session by rememberSaveable { mutableStateOf("") }
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text("Connect to Server")
                }
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            TextField(
                value = url,
                onValueChange = { url = it },
                label = {
                    Text("Server URL (Required)")
                }
            )
            TextField(
                value = session,
                onValueChange = { session = it },
                label = {
                    Text("Session ID (Optional)")
                }
            )
            Button(onClick = { onConnect(url, session.takeIf { it.isNotBlank() }) }) {
                Text("Connect")
            }
        }
    }
}
