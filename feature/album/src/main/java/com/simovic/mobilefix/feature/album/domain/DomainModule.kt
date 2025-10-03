package com.simovic.mobilefix.feature.album.domain

import com.simovic.mobilefix.feature.album.domain.usecase.GetAlbumListUseCase
import com.simovic.mobilefix.feature.album.domain.usecase.GetAlbumUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule =
    module {

        singleOf(::GetAlbumListUseCase)

        singleOf(::GetAlbumUseCase)
    }
