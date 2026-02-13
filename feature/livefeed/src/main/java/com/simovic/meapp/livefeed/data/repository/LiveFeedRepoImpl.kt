package com.simovic.meapp.livefeed.data.repository

import com.simovic.meapp.feature.base.domain.result.Result
import com.simovic.meapp.livefeed.data.datasource.api.service.LiveFeedService
import com.simovic.meapp.livefeed.data.mapper.LiveFeedMapper
import com.simovic.meapp.livefeed.domain.model.FeedItem
import com.simovic.meapp.livefeed.domain.repository.LiveFeedRepo
import timber.log.Timber

internal class LiveFeedRepoImpl(
    private val service: LiveFeedService,
    private val mapper: LiveFeedMapper,
) : LiveFeedRepo {
    override suspend fun getFeed(): Result<List<FeedItem>?> =
        try {
            Result.Success(mapper.rssToDomain(service.getFeed()))
        } catch (e: Exception) {
            Timber.d("::DEVELOP:: Exception is ${e.message}")
            Result.Failure(e)
        }
}
