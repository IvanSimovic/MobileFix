package com.simovic.mobilefix.feature.album.domain.usecase

import com.simovic.mobilefix.feature.album.domain.repository.AlbumRepository
import com.simovic.mobilefix.feature.base.domain.result.Result

internal class ToggleAlbumFavoriteStatusUseCase(
    private val albumRepository: AlbumRepository,
) {
    suspend operator fun invoke(albumMbId: String, isFavorite: Boolean): Result<Unit> {
        return if (isFavorite) {
            albumRepository.removeAlbumFromFavorites(albumMbId)
        } else {
            albumRepository.addAlbumToFavorites(albumMbId)
        }
    }
}
