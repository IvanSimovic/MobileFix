package com.simovic.meapp.livefeed.presentation.screen.livefeed

import com.simovic.meapp.feature.base.presentation.viewmodel.BaseAction
import com.simovic.meapp.livefeed.domain.model.FeedItem

internal sealed interface LiveFeedAction : BaseAction<LiveFeedUiState> {
    object Start : LiveFeedAction {
        override fun reduce(state: LiveFeedUiState) = LiveFeedUiState.Loading
    }

    class Success(
        private val feed: List<FeedItem>,
    ) : LiveFeedAction {
        override fun reduce(state: LiveFeedUiState) = LiveFeedUiState.Content(feed)
    }

    object AlbumListLoadFailure : LiveFeedAction {
        override fun reduce(state: LiveFeedUiState) = LiveFeedUiState.Error
    }
}
