package com.simovic.meapp.livefeed.presentation

import com.simovic.meapp.livefeed.presentation.screen.livefeed.LiveFeedViewModel
import com.simovic.meapp.livefeed.presentation.screen.livefeeddetails.LiveFeedDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule =
    module {

        // Feed
        viewModelOf(::LiveFeedViewModel)
        viewModelOf(::LiveFeedDetailsViewModel)
    }
