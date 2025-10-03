package com.simovic.mobilefix.feature.favourite

import com.simovic.mobilefix.feature.favourite.data.dataModule
import com.simovic.mobilefix.feature.favourite.domain.domainModule
import com.simovic.mobilefix.feature.favourite.presentation.presentationModule

val featureFavouriteModules =
    listOf(
        presentationModule,
        domainModule,
        dataModule,
    )
