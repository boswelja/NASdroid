package com.nasdroid.apps.ui.discover.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.nasdroid.skeleton.skeleton

private const val ASPECT_RATIO_16_9 = 16/9f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScreenshotCarousel(
    screenshots: List<String>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    HorizontalUncontainedCarousel(
        state = rememberCarouselState {
            screenshots.size
        },
        itemWidth = 240.dp,
        itemSpacing = 8.dp,
        modifier = modifier,
        contentPadding = contentPadding,
    ) { index ->
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(screenshots[index])
                .crossfade(true)
                .build(),
            contentDescription = null,
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ASPECT_RATIO_16_9)
                        .skeleton(true),
                )
            }
        )
    }
}
