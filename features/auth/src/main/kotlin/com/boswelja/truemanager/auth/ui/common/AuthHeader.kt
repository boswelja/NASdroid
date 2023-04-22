package com.boswelja.truemanager.auth.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.auth.R
import com.google.accompanist.drawablepainter.rememberDrawablePainter


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
                            append(stringResource(R.string.auth_header_true))
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(stringResource(R.string.auth_header_manager))
                            }
                        },
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = stringResource(R.string.auth_header_label),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(start = 4.dp)
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
