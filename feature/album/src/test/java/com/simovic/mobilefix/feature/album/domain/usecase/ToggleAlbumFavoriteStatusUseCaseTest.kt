package com.simovic.mobilefix.feature.album.domain.usecase

import com.simovic.mobilefix.feature.album.domain.repository.AlbumRepository
import com.simovic.mobilefix.feature.base.domain.result.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class ToggleAlbumFavoriteStatusUseCaseTest {
    private val mockAlbumRepository: AlbumRepository = mockk()

    private val sut = ToggleAlbumFavoriteStatusUseCase(mockAlbumRepository)

    @Test
    fun `ToggleAlbumFavoriteStatusUseCase when album is favorited calls removeAlbumFromFavorites and returns success`() =
        runBlocking {
            // given
            val albumMbId = "test-mbid-1"
            val isFavorite = true

            coEvery { mockAlbumRepository.removeAlbumFromFavorites(albumMbId) } returns Result.Success(Unit)

            // when
            val actual = sut.invoke(albumMbId, isFavorite)

            // then
            coVerify(exactly = 1) { mockAlbumRepository.removeAlbumFromFavorites(albumMbId) }
            coVerify(exactly = 0) { mockAlbumRepository.addAlbumToFavorites(any()) }
            actual shouldBeEqualTo Result.Success(Unit)
        }

    @Test
    fun `ToggleAlbumFavoriteStatusUseCase when album is not favorited calls addAlbumToFavorites and returns success`() =
        runBlocking {
            // given
            val albumMbId = "test-mbid-2"
            val isFavorite = false

            coEvery { mockAlbumRepository.addAlbumToFavorites(albumMbId) } returns Result.Success(Unit)

            // when
            val actual = sut.invoke(albumMbId, isFavorite)

            // then
            coVerify(exactly = 0) { mockAlbumRepository.removeAlbumFromFavorites(any()) }
            coVerify(exactly = 1) { mockAlbumRepository.addAlbumToFavorites(albumMbId) }
            actual shouldBeEqualTo Result.Success(Unit)
        }

    @Test
    fun `ToggleAlbumFavoriteStatusUseCase when removeAlbumFromFavorites fails returns failure`() =
        runBlocking {
            // given
            val albumMbId = "test-mbid-3"
            val isFavorite = true
            val exception = Exception("Remove failed")

            coEvery { mockAlbumRepository.removeAlbumFromFavorites(albumMbId) } returns Result.Failure(exception)

            // when
            val actual = sut.invoke(albumMbId, isFavorite)

            // then
            coVerify(exactly = 1) { mockAlbumRepository.removeAlbumFromFavorites(albumMbId) }
            actual shouldBeEqualTo Result.Failure(exception)
        }

    @Test
    fun `ToggleAlbumFavoriteStatusUseCase when addAlbumToFavorites fails returns failure`() =
        runBlocking {
            // given
            val albumMbId = "test-mbid-4"
            val isFavorite = false
            val exception = Exception("Add failed")

            coEvery { mockAlbumRepository.addAlbumToFavorites(albumMbId) } returns Result.Failure(exception)

            // when
            val actual = sut.invoke(albumMbId, isFavorite)

            // then
            coVerify(exactly = 1) { mockAlbumRepository.addAlbumToFavorites(albumMbId) }
            actual shouldBeEqualTo Result.Failure(exception)
        }
}
