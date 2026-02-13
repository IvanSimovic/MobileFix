package com.simovic.meapp.feature.album.domain.usecase

import com.simovic.meapp.feature.album.domain.repository.AlbumRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class IsAlbumFavoriteUseCaseTest {
    private val mockAlbumRepository: AlbumRepository = mockk()
    private val sut = IsAlbumFavoriteUseCase(mockAlbumRepository)

    @Test
    fun `IsAlbumFavoriteUseCase when repository returns true returns true`() {
        runBlocking {
            // given
            val mbId = "mbId-123"
            coEvery { mockAlbumRepository.isAlbumFavorite(mbId) } returns true

            // when
            val actual = sut.invoke(mbId)

            // then
            coVerify(exactly = 1) { mockAlbumRepository.isAlbumFavorite(mbId) }
            actual shouldBeEqualTo true
        }
    }

    @Test
    fun `IsAlbumFavoriteUseCase when repository returns false returns false`() {
        runBlocking {
            // given
            val mbId = "mbId-456"
            coEvery { mockAlbumRepository.isAlbumFavorite(mbId) } returns false

            // when
            val actual = sut.invoke(mbId)

            // then
            coVerify(exactly = 1) { mockAlbumRepository.isAlbumFavorite(mbId) }
            actual shouldBeEqualTo false
        }
    }
}
