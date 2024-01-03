package com.nasdroid.apps.ui.installed.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun InstalledAppDetailsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: InstalledAppDetailsViewModel = koinViewModel()
) {
    val appDetails by viewModel.appDetails.collectAsState()
    val appNotes by viewModel.appNotes.collectAsState()

    AnimatedContent(
        targetState = appDetails,
        label = "Installed App Details Content"
    ) {
        if (it != null) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(300.dp),
                modifier = modifier,
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp
            ) {
                item {
                    ApplicationInfo(installedAppDetails = it)
                }
                appNotes?.let {
                    item {
                        ApplicationNotes(note = it)
                    }
                }
            }
        }
    }
}
