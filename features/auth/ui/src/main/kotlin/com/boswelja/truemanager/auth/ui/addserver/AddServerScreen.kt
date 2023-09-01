package com.boswelja.truemanager.auth.ui.addserver

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.auth.ui.serverselect.AppBranding

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    // TODO Different layouts based on device type
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        AppBranding(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 180.dp)
                .aspectRatio(2f)
                .padding(contentPadding)
        )
        AuthComponents(onLoginSuccess, Modifier.fillMaxSize(), contentPadding)
    }
}
