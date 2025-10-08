package com.simovic.mobilefix.feature.album.domain.usecase

import com.simovic.mobilefix.feature.album.data.repository.AlbumRepositoryImpl
import com.simovic.mobilefix.feature.album.domain.DomainFixtures
import com.simovic.mobilefix.feature.base.domain.result.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class GetAlbumListUseCaseTest {
    private val mockAlbumRepository: AlbumRepositoryImpl = mockk()

    private val sut = GetAlbumListUseCase(mockAlbumRepository)

    @Test
    fun `GetAlbumListUseCase when repository is successful returns list of albums`() {
        // given
        val albums = listOf(DomainFixtures.getAlbum(), DomainFixtures.getAlbum())
        coEvery { mockAlbumRepository.searchAlbum(any()) } returns Result.Success(albums)

        // when
        val actual = runBlocking { sut(null) }

        // then
        actual shouldBeEqualTo Result.Success(albums)
    }

    @Test
    fun `GetAlbumListUseCase when onInit is called with no value uses null as default query search term`() =
        runBlocking {
            // given
            val albums = listOf(DomainFixtures.getAlbum(), DomainFixtures.getAlbum())
            coEvery { mockAlbumRepository.searchAlbum(any()) } returns Result.Success(albums)

            sut(null)

            coVerify { mockAlbumRepository.searchAlbum(null) }
        }

    @Test
    fun `GetAlbumListUseCase filters albums with default image`() {
        // given
        val albumWithImage = DomainFixtures.getAlbum()
        val albumWithoutImage = DomainFixtures.getAlbum(images = listOf())
        val albums = listOf(albumWithImage, albumWithoutImage)
        coEvery { mockAlbumRepository.searchAlbum(any()) } returns Result.Success(albums)

        // when
        val actual = runBlocking { sut(null) }

        // then
        actual shouldBeEqualTo Result.Success(listOf(albumWithImage))
    }

    @Test
    fun `GetAlbumListUseCase when repository throws an exception returns error`() {
        // given
        val resultFailure = mockk<Result.Failure>()
        coEvery { mockAlbumRepository.searchAlbum(any()) } returns resultFailure

        // when
        val actual = runBlocking { sut(null) }

        // then
        actual shouldBeEqualTo resultFailure
    }
}
