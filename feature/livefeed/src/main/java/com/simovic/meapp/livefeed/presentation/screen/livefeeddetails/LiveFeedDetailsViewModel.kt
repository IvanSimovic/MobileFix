package com.simovic.meapp.livefeed.presentation.screen.livefeeddetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.simovic.meapp.feature.base.presentation.navigation.NavigationScreens
import com.simovic.meapp.feature.base.presentation.viewmodel.BaseViewModel
import com.simovic.meapp.livefeed.domain.model.FeedItem
import kotlinx.coroutines.launch

internal class LiveFeedDetailsViewModel(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<LiveFeedDetailsUiState, LiveFeedDetailsAction>(LiveFeedDetailsUiState.Loading) {
    init {
        viewModelScope.launch {
            val feedItem = savedStateHandle.toRoute<NavigationScreens.LiveFeedDetail>()
            sendAction(
                LiveFeedDetailsAction.Content(
                    FeedItem(
                        guid = feedItem.guid,
                        title = feedItem.title,
                        link = feedItem.link,
                        pubDate = feedItem.pubDate,
                        description = feedItem.description,
                    ),
                ),
            )
        }
    }
}
