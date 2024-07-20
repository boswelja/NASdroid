package com.nasdroid.apps.ui.discover.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.boswelja.markdown.material3.MarkdownDocument
import com.nasdroid.apps.logic.discover.AvailableApp
import com.nasdroid.apps.logic.discover.AvailableAppDetails
import com.nasdroid.apps.ui.discover.AvailableAppCard
import com.nasdroid.apps.ui.installed.details.AppIcon
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.navigation.BackNavigationScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun AvailableAppDetailsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAppDetails: (id: String, catalog: String, train: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AvailableAppDetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val appDetails by viewModel.appDetails.collectAsState()
    val similarApps by viewModel.similarApps.collectAsState()

    BackNavigationScaffold(
        title = {
            appDetails?.name?.let { Text(it) }
        },
        onNavigateBack = onNavigateBack
    ) {
        appDetails?.let { details ->
            AvailableAppDetailsVerticalContent(
                details = details,
                similarApps = similarApps,
                onSimilarAppClick = {
                    onNavigateToAppDetails(it.id, it.catalogName, it.catalogTrain)
                },
                modifier = modifier.padding(it),
                contentPadding = PaddingValues(
                    horizontal = MaterialThemeExt.paddings.medium,
                    vertical = MaterialThemeExt.paddings.small
                )
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AvailableAppDetailsVerticalContent(
    details: AvailableAppDetails,
    similarApps: List<AvailableApp>,
    onSimilarAppClick: (AvailableApp) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.small),
                modifier = Modifier.fillMaxWidth()
            ) {
                AppIcon(
                    iconUrl = details.iconUrl,
                    modifier = Modifier
                        .size(128.dp)
                        .clip(MaterialThemeExt.shapes.medium)
                )
                MarkdownDocument(
                    markdown = details.description,
                    modifier = Modifier.widthIn(min = 120.dp, max = 480.dp)
                )
            }
        }
        if (details.screenshots.isNotEmpty()) {
            item {
                Text("Screenshots")
            }
            item {
                ScreenshotCarousel(details.screenshots)
            }
        }
        item {
            Text("Application Info")
        }
        item {
            Column {
                Text("Version: ${details.version}")
                Text("Source: ${details.sources.first()}")
                Text("Last App Update: ${details.lastUpdatedAt}")
            }
        }
        item {
            Text("Helm Chart Info")
        }
        item {
            Column {
                Text("Catalog: ${details.chartDetails.catalog}")
                Text("Train: ${details.chartDetails.train}")
                Text("Chart Version: ${details.chartDetails.chartVersion}")
                Text("Maintainer: ${details.chartDetails.maintainers.first().name} (${details.chartDetails.maintainers.first().email})")
            }
        }
        if (similarApps.isNotEmpty()) {
            item {
                Text("Similar Apps")
            }
            items(similarApps) { availableApp ->
                AvailableAppCard(
                    onClick = { onSimilarAppClick(availableApp) },
                    app = availableApp,
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
