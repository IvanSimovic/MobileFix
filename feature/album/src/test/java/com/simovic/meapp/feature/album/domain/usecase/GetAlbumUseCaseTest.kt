package com.simovic.meapp.feature.album.domain.usecase

import com.simovic.meapp.feature.album.data.repository.AlbumRepositoryImpl
import com.simovic.meapp.feature.album.domain.model.Album
import com.simovic.meapp.feature.base.domain.result.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class GetAlbumUseCaseTest {
    private val mockAlbumRepository: AlbumRepositoryImpl = mockk()

    private val sut = GetAlbumUseCase(mockAlbumRepository)

    @Test
    fun `GetAlbumUseCase when repository is successful returns album`() {
        // given
        val albumName = "Thriller"
        val artistName = "Michael Jackson"
        val mbId = "123"

        val album = mockk<Album>()
        coEvery { mockAlbumRepository.getAlbumInfo(artistName, albumName, mbId) } answers { Result.Success(album) }

        // when
        val actual = runBlocking { sut(artistName, albumName, mbId) }

        // then
        actual shouldBeEqualTo Result.Success(album)
    }

    @Test
    fun `GetAlbumUseCase when repository fails returns error`() {
        // given
        val albumName = "Thriller"
        val artistName = "Michael Jackson"
        val mbId = "123"
        val resultFailure = mockk<Result.Failure>()

        coEvery { mockAlbumRepository.getAlbumInfo(artistName, albumName, mbId) } returns
            resultFailure

        // when
        val actual = runBlocking { sut(artistName, albumName, mbId) }

        // then
        coVerify { mockAlbumRepository.getAlbumInfo(artistName, albumName, mbId) }
        actual shouldBeEqualTo resultFailure
    }
}
