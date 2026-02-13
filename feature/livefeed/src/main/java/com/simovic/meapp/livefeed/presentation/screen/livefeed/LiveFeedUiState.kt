package com.simovic.meapp.livefeed.presentation.screen.livefeed

import androidx.compose.runtime.Immutable
import com.simovic.meapp.feature.base.presentation.viewmodel.BaseState
import com.simovic.meapp.livefeed.domain.model.FeedItem

@Immutable
internal sealed interface LiveFeedUiState : BaseState {
    @Immutable
    data class Content(
        val feed: List<FeedItem>,
    ) : LiveFeedUiState

    @Immutable
    data object Loading : LiveFeedUiState

    @Immutable
    data object Error : LiveFeedUiState
}
