package com.simovic.meapp.livefeed.presentation.screen.livefeeddetails

import androidx.compose.runtime.Immutable
import com.simovic.meapp.feature.base.presentation.viewmodel.BaseState
import com.simovic.meapp.livefeed.domain.model.FeedItem

@Immutable
internal sealed interface LiveFeedDetailsUiState : BaseState {
    @Immutable
    data object Loading : LiveFeedDetailsUiState

    @Immutable
    data class Content(
        val feed: FeedItem,
    ) : LiveFeedDetailsUiState
}
