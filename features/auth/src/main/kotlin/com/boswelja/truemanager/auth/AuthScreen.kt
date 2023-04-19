package com.boswelja.truemanager.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    // TODO Different layouts based on device type
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        AuthComponents(Modifier.fillMaxSize(), contentPadding)
    }
}
