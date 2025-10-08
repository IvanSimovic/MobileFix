package com.simovic.mobilefix.feature.album.domain

import com.simovic.mobilefix.feature.album.domain.usecase.GetAlbumListUseCase
import com.simovic.mobilefix.feature.album.domain.usecase.GetAlbumUseCase
import com.simovic.mobilefix.feature.album.domain.usecase.IsAlbumFavoriteUseCase
import com.simovic.mobilefix.feature.album.domain.usecase.ToggleAlbumFavoriteStatusUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule =
    module {

        singleOf(::GetAlbumListUseCase)

        singleOf(::GetAlbumUseCase)

        singleOf(::ToggleAlbumFavoriteStatusUseCase)

        singleOf(::IsAlbumFavoriteUseCase)
    }
