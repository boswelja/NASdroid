package com.nasdroid.apps.ui.discover.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.nasdroid.apps.logic.discover.AvailableApp
import com.nasdroid.apps.logic.discover.AvailableAppDetails
import com.nasdroid.apps.ui.discover.AvailableAppCard
import com.nasdroid.apps.ui.installed.details.AppIcon
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.design.NasDroidTheme
import com.nasdroid.design.plus
import com.nasdroid.navigation.BackNavigationScaffold
import kotlinx.datetime.Instant
import org.koin.androidx.compose.koinViewModel

/**
 * Displays details about a single app that is available to install from a chart.
 */
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
        onNavigateBack = onNavigateBack,
        modifier = modifier
    ) { contentPadding ->
        AnimatedContent(
            targetState = state,
            label = "details content",
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { targetState ->
            when (targetState) {
                AvailableAppDetailsViewModel.State.Error.AppNotFound -> {
                    // TODO
                }
                AvailableAppDetailsViewModel.State.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(contentPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                AvailableAppDetailsViewModel.State.Success -> {
                    appDetails?.let { details ->
                        AvailableAppDetailsVerticalContent(
                            details = details,
                            similarApps = similarApps,
                            onSimilarAppClick = {
                                onNavigateToAppDetails(it.id, it.catalogName, it.catalogTrain)
                            },
                            contentPadding = PaddingValues(
                                horizontal = MaterialThemeExt.paddings.large,
                                vertical = MaterialThemeExt.paddings.small
                            ) + contentPadding
                        )
                    }
                }
            }
        }
    }
}

/**
 * Displays details about a single installable app in a vertically scrolling layout.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AvailableAppDetailsVerticalContent(
    details: AvailableAppDetails,
    similarApps: List<AvailableApp>,
    onSimilarAppClick: (AvailableApp) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val lazyListPadding = PaddingValues(
        top = contentPadding.calculateTopPadding(),
        bottom = contentPadding.calculateBottomPadding()
    )
    val itemHorizontalPadding = PaddingValues(
        start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
        end = contentPadding.calculateEndPadding(LocalLayoutDirection.current)
    )
    LazyColumn(
        modifier = modifier,
        contentPadding = lazyListPadding,
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.small)
    ) {
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.small),
                modifier = Modifier.fillMaxWidth().padding(itemHorizontalPadding)
            ) {
                AppIcon(
                    iconUrl = details.iconUrl,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(MaterialThemeExt.shapes.medium)
                )
                Text(text = details.description)
            }
        }
        if (details.tags.isNotEmpty()) {
            item {
                KeywordsRow(
                    keywords = details.tags,
                    modifier = Modifier.padding(itemHorizontalPadding)
                )
            }
        }
        if (details.screenshots.isNotEmpty()) {
            item {
                ScreenshotCarousel(
                    screenshots = details.screenshots,
                    contentPadding = itemHorizontalPadding
                )
            }
        }
        item {
            AppInfo(
                homepage = details.homepage,
                version = details.version,
                lastUpdatedAt = details.lastUpdatedAt,
                sources = details.sources,
                modifier = Modifier.padding(itemHorizontalPadding)
            )
        }
        item {
            HelmChartInfo(
                chartInfo = details.chartDetails,
                modifier = Modifier.padding(itemHorizontalPadding)
            )
        }
        if (similarApps.isNotEmpty()) {
            item {
                Text(
                    text = "Similar Apps",
                    style = MaterialThemeExt.typography.titleMedium,
                    modifier = Modifier.padding(itemHorizontalPadding)
                )
            }
            items(similarApps) { availableApp ->
                AvailableAppCard(
                    onClick = { onSimilarAppClick(availableApp) },
                    app = availableApp,
                    modifier = Modifier
                        .height(128.dp)
                        .fillMaxWidth()
                        .padding(itemHorizontalPadding)
                )
            }
        }
    }
}

@Composable
internal fun AvailableAppListItem(
    label: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .heightIn(56.dp)
            .semantics(mergeDescendants = true) {},
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            style = MaterialThemeExt.typography.labelSmall,
            color = MaterialThemeExt.colorScheme.onSurfaceVariant
        )
        Text(
            text = content,
            style = MaterialThemeExt.typography.bodyLarge,
            color = MaterialThemeExt.colorScheme.contentColorFor(LocalContentColor.current)
        )
    }
}

@PreviewLightDark
@Composable
fun AvailableAppDetailsVerticalContentPreview() {
    NasDroidTheme {
        Surface {
            AvailableAppDetailsVerticalContent(
                details = AvailableAppDetails(
                    id = "123",
                    iconUrl = "https://placehold.co/512",
                    name = "Test App",
                    version = "2077.1",
                    tags = listOf("test", "app", "preview"),
                    homepage = "https://google.com/",
                    description = "This is a test app for preview purposes.",
                    screenshots = listOf(
                        "https://placehold.co/600x400",
                        "https://placehold.co/600x400",
                        "https://placehold.co/600x400"
                    ),
                    sources = listOf("https://google.com/"),
                    lastUpdatedAt = Instant.fromEpochSeconds(1234567890L),
                    chartDetails = AvailableAppDetails.ChartDetails(
                        catalog = "catalog",
                        train = "train",
                        chartVersion = "1.2.3",
                        maintainers = listOf(
                            AvailableAppDetails.ChartDetails.Maintainer(
                                name = "John Doe",
                                url = "https://google.com/",
                                email = "john.doe@example.com"
                            )
                        )
                    )
                ),
                similarApps = listOf(
                    AvailableApp(
                        id = "app2",
                        title = "Another App",
                        description = "Description of Another App",
                        iconUrl = "https://placehold.co/512",
                        version = "1.0.0",
                        catalogName = "catalog",
                        catalogTrain = "train",
                        isInstalled = false,
                        lastUpdated = Instant.DISTANT_PAST
                    ),
                    AvailableApp(
                        id = "app3",
                        title = "Yet Another App",
                        description = "Description of Yet Another App",
                        iconUrl = "https://placehold.co/512",
                        version = "1.0.0",
                        catalogName = "catalog",
                        catalogTrain = "train",
                        isInstalled = false,
                        lastUpdated = Instant.DISTANT_PAST
                    ),
                ),
                onSimilarAppClick = {},
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            )
        }
    }
}
