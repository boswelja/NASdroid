package com.nasdroid.apps.ui.discover.details

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nasdroid.apps.ui.discover.AvailableAppCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableAppDetailsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: AvailableAppDetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val appDetails by viewModel.appDetails.collectAsState()
    val similarApps by viewModel.similarApps.collectAsState()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 280.dp),
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            Text("Screenshots")
        }
        item(span = StaggeredGridItemSpan.FullLine) {
            appDetails?.let { details ->
                HorizontalUncontainedCarousel(state = rememberCarouselState {
                    details.screenshots.size
                }, itemWidth = 240.dp) {
                    AsyncImage(
                        model = details.screenshots[it],
                        contentDescription = null
                    )
                }
            }
        }
        item(span = StaggeredGridItemSpan.FullLine) {
            Text("Similar Apps")
        }
        items(similarApps) {
            AvailableAppCard(
                onClick = { /*TODO*/ },
                app = it,
                modifier = Modifier
                    .height(128.dp)
                    .fillMaxWidth()
            )
        }
    }
}
