package com.simovic.meapp.feature.album.domain.usecase

import com.simovic.meapp.feature.album.domain.model.Album
import com.simovic.meapp.feature.album.domain.repository.AlbumRepository
import com.simovic.meapp.feature.base.domain.result.Result
import com.simovic.meapp.feature.base.domain.result.mapSuccess

internal class GetAlbumListUseCase(
    private val albumRepository: AlbumRepository,
) {
    suspend operator fun invoke(query: String?): Result<List<Album>> {
        val result =
            albumRepository
                .searchAlbum(query)
                .mapSuccess {
                    val albumsWithImages = value.filter { it.getDefaultImageUrl() != null }

                    copy(value = albumsWithImages)
                }

        return result
    }
}
