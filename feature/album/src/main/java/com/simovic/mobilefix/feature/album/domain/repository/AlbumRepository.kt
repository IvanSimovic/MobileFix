package com.simovic.mobilefix.feature.album.domain.repository

import com.simovic.mobilefix.feature.album.domain.model.Album
import com.simovic.mobilefix.feature.base.domain.result.Result

internal interface AlbumRepository {
    suspend fun getAlbumInfo(
        artistName: String,
        albumName: String,
        mbId: String?,
    ): Result<Album>

    suspend fun searchAlbum(phrase: String?): Result<List<Album>>
}
