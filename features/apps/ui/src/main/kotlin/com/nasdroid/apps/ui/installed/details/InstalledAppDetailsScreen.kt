package com.nasdroid.apps.ui.installed.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

/**
 * A screen that displays detailed information about a single installed application.
 */
@Composable
fun InstalledAppDetailsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: InstalledAppDetailsViewModel = koinViewModel()
) {
    val appDetails by viewModel.appDetails.collectAsState()

    AnimatedContent(
        targetState = appDetails,
        label = "Installed App Details Content",
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) {
        if (it != null) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(240.dp),
                modifier = modifier,
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalItemSpacing = 16.dp
            ) {
                item {
                    ApplicationInfo(installedAppDetails = it)
                }
                it.notes?.let {
                    item {
                        ApplicationNotes(note = it)
                    }
                }
            }
        } else {
            Box(modifier) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}
