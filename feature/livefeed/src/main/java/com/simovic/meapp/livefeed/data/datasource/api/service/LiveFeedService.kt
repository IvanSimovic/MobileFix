package com.simovic.meapp.livefeed.data.datasource.api.service

import com.simovic.meapp.livefeed.data.datasource.api.model.RssFeed
import retrofit2.http.GET

internal interface LiveFeedService {
    @GET("feed/")
    suspend fun getFeed(): RssFeed
}
