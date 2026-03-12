package com.simovic.meapp.livefeed.presentation.screen.livefeed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.HtmlCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simovic.meapp.feature.base.common.res.Dimen
import com.simovic.meapp.feature.base.presentation.compose.composable.AppPreview
import com.simovic.meapp.feature.base.presentation.compose.composable.ErrorAnim
import com.simovic.meapp.feature.base.presentation.compose.composable.LoadingIndicator
import com.simovic.meapp.feature.base.presentation.ui.AppTheme
import com.simovic.meapp.feature.base.util.formatRssDate
import com.simovic.meapp.livefeed.domain.model.FeedItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun LiveFeedScreen(
    onNavigateToDetails: (FeedItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: LiveFeedViewModel = koinViewModel()

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getFeed()
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (val currentUiState = uiState) {
            LiveFeedUiState.Error -> ErrorAnim(modifier = Modifier.align(Alignment.Center))
            LiveFeedUiState.Loading -> LoadingIndicator(modifier = Modifier.align(Alignment.Center))
            is LiveFeedUiState.Content -> FeedContent(currentUiState, onNavigateToDetails)
        }
    }
}

@Composable
private fun FeedContent(
    uiState: LiveFeedUiState.Content,
    onClick: (FeedItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimen.spaceM),
        verticalArrangement = Arrangement.spacedBy(Dimen.spaceM),
    ) {
        itemsIndexed(uiState.feed, key = { _, item -> item.guid }) { _, item ->
            FeedCard(item = item, onClick = onClick)
        }
    }
}

@Composable
private fun FeedCard(
    item: FeedItem,
    onClick: (FeedItem) -> Unit,
) {
    ElevatedCard(
        onClick = { onClick(item) },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier =
                    Modifier
                        .width(Dimen.spaceS)
                        .fillMaxHeight()
                        .background(AppTheme.color.brandPrimary),
            )
            Column(
                modifier =
                    Modifier
                        .padding(Dimen.spaceL)
                        .weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimen.spaceS),
            ) {
                Text(
                    item.pubDate.formatRssDate(),
                    style = AppTheme.typo.body3,
                    color = AppTheme.color.textMuted,
                )
                Text(
                    HtmlCompat.fromHtml(item.title, HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
                    style = AppTheme.typo.head4,
                    color = AppTheme.color.textMain,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    HtmlCompat.fromHtml(item.description, HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
                    style = AppTheme.typo.body2,
                    color = AppTheme.color.textMuted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
private fun FeedContentPreview() {
    val previewLink = "https://indiegamesplus.com"
    val sampleData =
        listOf(
            FeedItem(
                guid = "1",
                title = "&#8216;The Well&#8217;s Blessing&#8217; Solves Audio Puzzles With Old Friends",
                link = previewLink,
                pubDate = "Tue, 03 Mar 2026 11:00:00 +0000",
                description = "<p>The Well&#8217;s Blessing sees you running into mysterious birds.</p>",
            ),
            FeedItem(
                guid = "2",
                title = "Cozy City Builder Goes Into Early Access Next Month",
                link = previewLink,
                pubDate = "Wed, 04 Mar 2026 09:30:00 +0000",
                description = "<p>A relaxing city builder with pastel visuals and no failure states.</p>",
            ),
            FeedItem(
                guid = "3",
                title = "Upcoming Puzzle Game Demo Available This Weekend",
                link = previewLink,
                pubDate = "Thu, 05 Mar 2026 14:00:00 +0000",
                description = "<p>Players can try out the first three levels ahead of its full launch.</p>",
            ),
        )

    AppPreview {
        FeedContent(
            uiState = LiveFeedUiState.Content(sampleData),
            onClick = { },
        )
    }
}
