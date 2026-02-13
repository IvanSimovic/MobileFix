package com.simovic.meapp.feature.album

import com.simovic.meapp.feature.album.data.dataModule
import com.simovic.meapp.feature.album.domain.domainModule
import com.simovic.meapp.feature.album.presentation.presentationModule

val featureAlbumModules =
    listOf(
        presentationModule,
        domainModule,
        dataModule,
    )
