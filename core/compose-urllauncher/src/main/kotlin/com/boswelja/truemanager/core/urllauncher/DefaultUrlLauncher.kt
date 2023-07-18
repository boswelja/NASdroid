package com.boswelja.truemanager.core.urllauncher

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

internal class DefaultUrlLauncher(private val context: Context) : UrlLauncher {
    override fun launchUrl(url: String) {
        val intent = CustomTabsIntent.Builder()
            .build()
        intent.launchUrl(context, Uri.parse(url))
    }
}
