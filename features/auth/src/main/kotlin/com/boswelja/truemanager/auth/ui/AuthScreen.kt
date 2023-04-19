package com.boswelja.truemanager.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter

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
        AuthHeader(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .padding(contentPadding)
        )
        AuthComponents(Modifier.fillMaxSize(), contentPadding)
    }
}

@Composable
fun AuthHeader(
    modifier: Modifier = Modifier
) {
    Surface(color = MaterialTheme.colorScheme.primaryContainer) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = rememberAppIconPainter(), contentDescription = null)
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = buildAnnotatedString {
                            append("True")
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append("Manager")
                            }
                        },
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = "for TrueNAS SCALE",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberAppIconPainter(): Painter {
    val context = LocalContext.current
    val icon = remember(context) { context.packageManager.getApplicationIcon(context.packageName) }
    return rememberDrawablePainter(drawable = icon)
}