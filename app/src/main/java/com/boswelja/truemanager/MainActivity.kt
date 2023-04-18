package com.boswelja.truemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.boswelja.truemanager.auth.AuthScreen
import com.boswelja.truemanager.ui.theme.TrueManagerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrueManagerTheme {
                Scaffold {
                    AuthScreen(Modifier.fillMaxSize().padding(it))
                }
            }
        }
    }
}
