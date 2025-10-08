package com.simovic.mobilefix.feature.album.domain.usecase

import com.simovic.mobilefix.feature.album.domain.repository.AlbumRepository

internal class IsAlbumFavoriteUseCase(
    private val albumRepository: AlbumRepository,
) {
    suspend operator fun invoke(albumMbId: String): Boolean = albumRepository.isAlbumFavorite(albumMbId)
}
