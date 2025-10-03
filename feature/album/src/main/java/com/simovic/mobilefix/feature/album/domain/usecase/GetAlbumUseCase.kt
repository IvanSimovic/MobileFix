package com.simovic.mobilefix.feature.album.domain.usecase

import com.simovic.mobilefix.feature.album.domain.model.Album
import com.simovic.mobilefix.feature.album.domain.repository.AlbumRepository
import com.simovic.mobilefix.feature.base.domain.result.Result

internal class GetAlbumUseCase(
    private val albumRepository: AlbumRepository,
) {
    suspend operator fun invoke(
        artistName: String,
        albumName: String,
        mbId: String?,
    ): Result<Album> = albumRepository.getAlbumInfo(artistName, albumName, mbId)
}
