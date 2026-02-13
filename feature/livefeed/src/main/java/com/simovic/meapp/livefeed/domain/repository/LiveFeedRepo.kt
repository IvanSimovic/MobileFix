package com.simovic.meapp.livefeed.domain.repository

import com.simovic.meapp.feature.base.domain.result.Result
import com.simovic.meapp.livefeed.domain.model.FeedItem

internal interface LiveFeedRepo {
    suspend fun getFeed(): Result<List<FeedItem>?>
}
