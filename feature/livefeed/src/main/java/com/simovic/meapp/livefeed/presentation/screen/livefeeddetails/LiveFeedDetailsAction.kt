package com.simovic.meapp.livefeed.presentation.screen.livefeeddetails

import com.simovic.meapp.feature.base.presentation.viewmodel.BaseAction
import com.simovic.meapp.livefeed.domain.model.FeedItem

internal sealed interface LiveFeedDetailsAction : BaseAction<LiveFeedDetailsUiState> {
    class Content(
        private val feed: FeedItem,
    ) : LiveFeedDetailsAction {
        override fun reduce(state: LiveFeedDetailsUiState) = LiveFeedDetailsUiState.Content(feed)
    }
}
