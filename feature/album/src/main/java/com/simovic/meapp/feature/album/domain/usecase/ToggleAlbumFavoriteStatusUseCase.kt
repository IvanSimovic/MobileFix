package com.simovic.meapp.feature.album.domain.usecase

import com.simovic.meapp.feature.album.domain.repository.AlbumRepository
import com.simovic.meapp.feature.base.domain.result.Result

internal class ToggleAlbumFavoriteStatusUseCase(
    private val albumRepository: AlbumRepository,
) {
    suspend operator fun invoke(
        albumMbId: String,
        isFavorite: Boolean,
    ): Result<Unit> =
        if (isFavorite) {
            albumRepository.removeAlbumFromFavorites(albumMbId)
        } else {
            albumRepository.addAlbumToFavorites(albumMbId)
        }
}
