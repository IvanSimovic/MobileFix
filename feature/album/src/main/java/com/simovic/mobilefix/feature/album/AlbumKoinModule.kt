package com.simovic.mobilefix.feature.album

import com.simovic.mobilefix.feature.album.data.dataModule
import com.simovic.mobilefix.feature.album.domain.domainModule
import com.simovic.mobilefix.feature.album.presentation.presentationModule

val featureAlbumModules =
    listOf(
        presentationModule,
        domainModule,
        dataModule,
    )
