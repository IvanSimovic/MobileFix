package com.simovic.meapp.feature.album.presentation.screen.albumdetail

import com.simovic.meapp.feature.album.data.DataFixtures
import com.simovic.meapp.feature.album.domain.model.Album
import com.simovic.meapp.feature.album.domain.usecase.GetAlbumUseCase
import com.simovic.meapp.feature.album.domain.usecase.IsAlbumFavoriteUseCase
import com.simovic.meapp.feature.album.domain.usecase.ToggleAlbumFavoriteStatusUseCase
import com.simovic.meapp.feature.base.domain.result.Result
import com.simovic.meapp.library.testutils.CoroutinesTestDispatcherExtension
import com.simovic.meapp.library.testutils.InstantTaskExecutorExtension
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestDispatcherExtension::class)
class AlbumDetailViewModelTest {
    private val mockGetAlbumUseCase: GetAlbumUseCase = mockk()
    private val mockToggleAlbumFavoriteStatusUseCase: ToggleAlbumFavoriteStatusUseCase = mockk()
    private val mockIsAlbumFavoriteUseCase: IsAlbumFavoriteUseCase = mockk()

    private val sut =
        AlbumDetailViewModel(
            mockGetAlbumUseCase,
            mockToggleAlbumFavoriteStatusUseCase,
            mockIsAlbumFavoriteUseCase,
        )

    @Test
    fun `onInit when album is not found returns error`() =
        runTest {
            // given
            val album = DataFixtures.getAlbumDomainModel()

            coEvery {
                mockGetAlbumUseCase.invoke(album.artist, album.name, album.mbId)
            } returns Result.Failure()

            // when
            sut.onInit(album.name, album.artist, album.mbId)

            // then
            advanceUntilIdle()
            sut.uiStateFlow.value shouldBeEqualTo AlbumDetailUiState.Error
        }

    @Test
    fun `onInit when album is found returns content`() =
        runTest {
            // given
            val album = DataFixtures.getAlbumDomainModel()
            coEvery { mockIsAlbumFavoriteUseCase.invoke(album.mbId!!) } returns false

            coEvery {
                mockGetAlbumUseCase.invoke(album.artist, album.name, album.mbId)
            } returns Result.Success(album)

            // when
            sut.onInit(album.name, album.artist, album.mbId)

            // then
            advanceUntilIdle()
            sut.uiStateFlow.value shouldBeEqualTo
                AlbumDetailUiState.Content(
                    albumName = album.name,
                    artistName = album.artist,
                    coverImageUrl = "",
                    tracks = null,
                    tags = null,
                    mbId = album.mbId,
                    isFavorite = false,
                )
        }

    @Test
    fun `addToFavorites when mbId is present and toggle is successful returns updated content`() =
        runTest {
            // given
            val album = DataFixtures.getAlbumDomainModel()

            coEvery { mockGetAlbumUseCase.invoke(any(), any(), any()) } returns Result.Success(album)
            coEvery { mockIsAlbumFavoriteUseCase.invoke(album.mbId!!) } returns false andThen true
            coEvery { mockToggleAlbumFavoriteStatusUseCase.invoke(album.mbId!!, false) } returns Result.Success(Unit)

            sut.onInit(album.name, album.artist, album.mbId)
            advanceUntilIdle()

            // when
            sut.addToFavorites()
            advanceUntilIdle()

            // then
            coVerify(exactly = 1) { mockToggleAlbumFavoriteStatusUseCase.invoke(album.mbId!!, false) }
            sut.uiStateFlow.value shouldBeEqualTo
                AlbumDetailUiState.Content(
                    albumName = album.name,
                    artistName = album.artist,
                    coverImageUrl = "",
                    tracks = null,
                    tags = null,
                    mbId = album.mbId,
                    isFavorite = true,
                )
        }

    @Test
    fun `addToFavorites when mbId is null does not call use case`() =
        runTest {
            // given
            val album = DataFixtures.getAlbumDomainModel(mbId = null)

            coEvery { mockGetAlbumUseCase.invoke(any(), any(), any()) } returns Result.Success(album)
            coEvery { mockToggleAlbumFavoriteStatusUseCase.invoke(any(), any()) } returns Result.Success(Unit)

            sut.onInit(album.name, album.artist, album.mbId)
            advanceUntilIdle()

            // when
            sut.addToFavorites()
            advanceUntilIdle()

            // then
            coVerify(exactly = 0) { mockToggleAlbumFavoriteStatusUseCase.invoke(any(), any()) }
        }

    @Test
    fun `addToFavorites when toggle fails returns error`() =
        runTest {
            // given
            val album = DataFixtures.getAlbumDomainModel()
            val exception = Exception("Toggle failed")

            coEvery { mockGetAlbumUseCase.invoke(any(), any(), any()) } returns Result.Success(album)
            coEvery { mockIsAlbumFavoriteUseCase.invoke(album.mbId!!) } returns false
            coEvery { mockToggleAlbumFavoriteStatusUseCase.invoke(album.mbId!!, false) } returns Result.Failure(exception)

            sut.onInit(album.name, album.artist, album.mbId)
            advanceUntilIdle()

            // when
            sut.addToFavorites()
            advanceUntilIdle()

            // then
            coVerify(exactly = 1) { mockToggleAlbumFavoriteStatusUseCase.invoke(album.mbId!!, false) }
            sut.uiStateFlow.value shouldBeEqualTo AlbumDetailUiState.Error
        }

    @Test
    fun `onInit uses mbId override when provided returns updated content`() =
        runTest {
            // given
            val apiAlbumMbId = "api-mbid-xyz"
            val overrideMbId = "override-mbid-123"
            val album: Album = DataFixtures.getAlbumDomainModel(mbId = apiAlbumMbId)

            coEvery { mockIsAlbumFavoriteUseCase.invoke(overrideMbId) } returns true
            coEvery { mockGetAlbumUseCase.invoke(album.artist, album.name, overrideMbId) } returns Result.Success(album)

            // when
            sut.onInit(album.name, album.artist, overrideMbId)
            advanceUntilIdle()

            // then
            sut.uiStateFlow.value shouldBeEqualTo
                AlbumDetailUiState.Content(
                    albumName = album.name,
                    artistName = album.artist,
                    coverImageUrl = "",
                    tracks = null,
                    tags = null,
                    mbId = overrideMbId,
                    isFavorite = true,
                )
        }
}
