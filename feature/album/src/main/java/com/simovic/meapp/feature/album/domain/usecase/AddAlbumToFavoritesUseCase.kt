package com.simovic.meapp.feature.album.domain.usecase

import com.simovic.meapp.feature.album.domain.repository.AlbumRepository

internal class AddAlbumToFavoritesUseCase(
    private val albumRepository: AlbumRepository,
) {
    suspend operator fun invoke(albumMbId: String) {
        albumRepository.addAlbumToFavorites(albumMbId)
    }
}
