package com.simovic.meapp.feature.album.domain.usecase

import com.simovic.meapp.feature.album.domain.model.Album
import com.simovic.meapp.feature.album.domain.repository.AlbumRepository
import com.simovic.meapp.feature.base.domain.result.Result

internal class GetAlbumUseCase(
    private val albumRepository: AlbumRepository,
) {
    suspend operator fun invoke(
        artistName: String,
        albumName: String,
        mbId: String?,
    ): Result<Album> = albumRepository.getAlbumInfo(artistName, albumName, mbId)
}
