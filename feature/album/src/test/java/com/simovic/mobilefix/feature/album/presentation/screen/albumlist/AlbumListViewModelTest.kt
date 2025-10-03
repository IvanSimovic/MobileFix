package com.simovic.mobilefix.feature.album.presentation.screen.albumlist

import androidx.lifecycle.SavedStateHandle
import com.simovic.mobilefix.feature.album.domain.model.Album
import com.simovic.mobilefix.feature.album.domain.usecase.GetAlbumListUseCase
import com.simovic.mobilefix.feature.base.domain.result.Result
import com.simovic.mobilefix.library.testutils.CoroutinesTestDispatcherExtension
import com.simovic.mobilefix.library.testutils.InstantTaskExecutorExtension
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestDispatcherExtension::class)
class AlbumListViewModelTest {
    private val mockGetAlbumListUseCase: GetAlbumListUseCase = mockk()

    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)

    private val sut =
        AlbumListViewModel(
            savedStateHandle,
            mockGetAlbumListUseCase,
        )

    @Test
    fun `onInit emits state error`() =
        runTest {
            // given
            coEvery { mockGetAlbumListUseCase.invoke("Jackson") } returns Result.Failure()

            // when
            sut.onInit("Jackson")

            // then
            advanceUntilIdle()

            sut.uiStateFlow.value shouldBeEqualTo AlbumListUiState.Error
        }

    @Test
    fun `onInit emits state success`() =
        runTest {
            // given
            val album = Album("albumName", "artistName")
            val albums = listOf(album)
            coEvery { mockGetAlbumListUseCase.invoke("Jackson") } returns Result.Success(albums)

            // when
            sut.onInit("Jackson")

            // then
            advanceUntilIdle()

            sut.uiStateFlow.value shouldBeEqualTo
                AlbumListUiState.Content(
                    albums = albums,
                )
        }
}
