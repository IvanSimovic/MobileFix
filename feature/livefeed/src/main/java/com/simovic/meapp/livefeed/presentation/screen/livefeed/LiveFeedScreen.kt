package com.simovic.meapp.livefeed.presentation.screen.livefeed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simovic.meapp.feature.base.presentation.compose.composable.AppPreview
import com.simovic.meapp.feature.base.presentation.compose.composable.ErrorAnim
import com.simovic.meapp.feature.base.presentation.compose.composable.LoadingIndicator
import com.simovic.meapp.feature.base.presentation.ui.AppTheme
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

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (val currentUiState = uiState) { // Extract to local variable for smart casting
            LiveFeedUiState.Error -> ErrorAnim()
            LiveFeedUiState.Loading -> LoadingIndicator()
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
    LazyColumn(modifier = modifier.padding(20.dp)) {
        itemsIndexed(uiState.feed, key = { _, item -> item.guid }) { index, item ->
            Column(modifier = Modifier.clickable { onClick(item) }) {
                Text(item.pubDate, style = AppTheme.typo.body3, color = AppTheme.color.textMuted)
                Text(item.title, style = AppTheme.typo.head2, color = AppTheme.color.textMain)
                Text(
                    item.description.substring(0, if (item.description.length < 20) item.description.length else 20),
                    style = AppTheme.typo.body2,
                    color = AppTheme.color.textMain,
                )
                if (index < uiState.feed.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = AppTheme.color.divider,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FeedGridPreview() {
    val sampleData =
        listOf(
            FeedItem(
                guid = "1",
                title = "Title 1",
                link = "link 1",
                pubDate = "05.04.1995",
                description = "Desc 1",
            ),
            FeedItem(
                guid = "2",
                title = "Title 2",
                link = "link 2",
                pubDate = "06.04.1995",
                description = "Desc 2",
            ),
        )

    AppPreview {
        FeedContent(
            uiState = LiveFeedUiState.Content(sampleData),
            onClick = { },
        )
    }
}
