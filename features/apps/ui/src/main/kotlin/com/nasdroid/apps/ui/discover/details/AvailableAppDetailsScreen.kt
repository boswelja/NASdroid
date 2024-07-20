package com.nasdroid.apps.ui.discover.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.nasdroid.apps.logic.discover.AvailableApp
import com.nasdroid.apps.logic.discover.AvailableAppDetails
import com.nasdroid.apps.ui.discover.AvailableAppCard
import com.nasdroid.navigation.BackNavigationScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun AvailableAppDetailsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AvailableAppDetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val appDetails by viewModel.appDetails.collectAsState()
    val similarApps by viewModel.similarApps.collectAsState()

    BackNavigationScaffold(
        title = {},
        onNavigateBack = onNavigateBack
    ) {
        appDetails?.let { details ->
            AvailableAppDetailsVerticalContent(
                details = details,
                similarApps = similarApps,
                modifier = modifier,
                contentPadding = it
            )
        }
    }
}

@Composable
fun AvailableAppDetailsVerticalContent(
    details: AvailableAppDetails,
    similarApps: List<AvailableApp>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        if (details.screenshots.isNotEmpty()) {
            item {
                Text("Screenshots")
            }
            item {
                ScreenshotCarousel(details.screenshots)
            }
        }
        if (similarApps.isNotEmpty()) {
            item {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScreenshotCarousel(
    screenshots: List<String>,
    modifier: Modifier = Modifier
) {
    HorizontalUncontainedCarousel(
        state = rememberCarouselState {
            screenshots.size
        },
        itemWidth = 240.dp,
        modifier = modifier,
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(screenshots[it])
                .crossfade(true)
                .build(),
            contentDescription = null,
            loading = {
                Box(
                    modifier = Modifier.fillMaxHeight().aspectRatio(16/9f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        )
    }
}
