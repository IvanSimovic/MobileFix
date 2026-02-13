package com.simovic.meapp.livefeed.presentation.screen.livefeed

import androidx.lifecycle.viewModelScope
import com.simovic.meapp.feature.base.domain.result.Result
import com.simovic.meapp.feature.base.presentation.viewmodel.BaseViewModel
import com.simovic.meapp.livefeed.domain.repository.LiveFeedRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class LiveFeedViewModel(
    private val liveFeedRepo: LiveFeedRepo,
) : BaseViewModel<LiveFeedUiState, LiveFeedAction>(LiveFeedUiState.Loading) {
    private var job: Job? = null

    fun getFeed() {
        if (job != null) {
            job?.cancel()
            job = null
        }

        job =
            viewModelScope.launch {
                liveFeedRepo.getFeed().also { result ->
                    val liveFeedAction =
                        when (result) {
                            is Result.Success -> {
                                result.value?.let { LiveFeedAction.Success(it) }
                                    ?: LiveFeedAction.AlbumListLoadFailure
                            }

                            is Result.Failure -> {
                                LiveFeedAction.AlbumListLoadFailure
                            }
                        }
                    sendAction(liveFeedAction)
                }
            }
    }
}
