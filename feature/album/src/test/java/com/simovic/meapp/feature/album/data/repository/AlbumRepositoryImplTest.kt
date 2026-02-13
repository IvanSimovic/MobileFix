package com.simovic.meapp.feature.album.data.repository

import com.simovic.meapp.feature.album.data.DataFixtures
import com.simovic.meapp.feature.album.data.datasource.api.response.GetAlbumInfoResponse
import com.simovic.meapp.feature.album.data.datasource.api.response.SearchAlbumResponse
import com.simovic.meapp.feature.album.data.datasource.api.service.AlbumRetrofitService
import com.simovic.meapp.feature.album.data.datasource.database.AlbumDao
import com.simovic.meapp.feature.album.data.datasource.database.model.FavoriteAlbumRoomModel
import com.simovic.meapp.feature.album.data.mapper.AlbumMapper
import com.simovic.meapp.feature.album.domain.model.Album
import com.simovic.meapp.feature.base.data.retrofit.ApiResult
import com.simovic.meapp.feature.base.domain.result.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import java.net.UnknownHostException

class AlbumRepositoryImplTest {
    private val mockService: AlbumRetrofitService = mockk()

    private val mockAlbumDao: AlbumDao = mockk(relaxed = true)

    private val mockAlbumMapper: AlbumMapper = mockk()

    private val sut = AlbumRepositoryImpl(mockService, mockAlbumDao, mockAlbumMapper)

    @Test
    fun `searchAlbum when api success returns albums`() {
        // given
        val phrase = "phrase"
        val mockAlbum = mockk<Album>()

        coEvery { mockService.searchAlbumAsync(phrase) } returns
            ApiResult.Success(
                DataFixtures.ApiResponse.getSearchAlbum(),
            )

        every { mockAlbumMapper.apiToRoom(any()) } returns mockk()
        every { mockAlbumMapper.apiToDomain(any()) } returns mockAlbum

        // when
        val actual = runBlocking { sut.searchAlbum(phrase) }

        // then
        actual shouldBeEqualTo Result.Success(listOf(mockAlbum))
    }

    @Test
    fun `searchAlbum when api success saves album in database`() {
        // given
        val phrase = "phrase"
        coEvery { mockService.searchAlbumAsync(phrase) } returns
            ApiResult.Success(
                DataFixtures.ApiResponse.getSearchAlbum(),
            )

        every { mockAlbumMapper.apiToRoom(any()) } returns mockk()
        every { mockAlbumMapper.apiToDomain(any()) } returns mockk()

        // when
        runBlocking { sut.searchAlbum(phrase) }

        // then
        coVerify { mockAlbumDao.insertAlbums(any()) }
    }

    @Test
    fun `searchAlbum when api exception fallbacks to database`() {
        // given
        val phrase = "phrase"
        val albumRoomModels = DataFixtures.getAlbumsRoomModels()
        val mockAlbum1 = mockk<Album>()
        val mockAlbum2 = mockk<Album>()

        coEvery { mockService.searchAlbumAsync(phrase) } returns ApiResult.Exception(UnknownHostException())
        coEvery { mockAlbumDao.getAll() } returns albumRoomModels
        every { mockAlbumMapper.roomToDomain(albumRoomModels[0]) } returns mockAlbum1
        every { mockAlbumMapper.roomToDomain(albumRoomModels[1]) } returns mockAlbum2

        // when
        val actual = runBlocking { sut.searchAlbum(phrase) }

        // then
        actual shouldBeEqualTo Result.Success(listOf(mockAlbum1, mockAlbum2))
    }

    @Test
    fun `searchAlbum when api error returns failure`() {
        // given
        val phrase = "phrase"

        coEvery { mockService.searchAlbumAsync(phrase) } returns mockk<ApiResult.Error<SearchAlbumResponse>>()

        // when
        val actual = runBlocking { sut.searchAlbum(phrase) }

        // then
        actual shouldBeEqualTo Result.Failure()
    }

    @Test
    fun `getAlbumInfo when api success returns album`() {
        // given
        val artistName = "Michael Jackson"
        val albumName = "Thriller"
        val mbId = "123"
        val album = DataFixtures.getAlbumApiModel(mbId, albumName, artistName)
        val mockAlbum = mockk<Album>()

        coEvery {
            mockService.getAlbumInfoAsync(artistName, albumName, mbId)
        } returns
            ApiResult.Success(
                GetAlbumInfoResponse(album),
            )

        every { mockAlbumMapper.apiToDomain(album) } returns mockAlbum

        // when
        val actual = runBlocking { sut.getAlbumInfo(artistName, albumName, mbId) }

        // then
        actual shouldBeEqualTo Result.Success(mockAlbum)
    }

    @Test
    fun `getAlbumInfo when api exception fallbacks to database`() {
        // given
        val artistName = "Michael Jackson"
        val albumName = "Thriller"
        val mbId = "123"

        coEvery {
            mockService.getAlbumInfoAsync(artistName, albumName, mbId)
        } returns ApiResult.Exception(UnknownHostException())

        coEvery { mockAlbumDao.getAlbum(artistName, albumName, mbId) } returns mockk()
        every { mockAlbumMapper.roomToDomain(any()) } returns mockk()

        // when
        runBlocking { sut.getAlbumInfo(artistName, albumName, mbId) }

        // then
        coVerify { mockAlbumDao.getAlbum(artistName, albumName, mbId) }
    }

    @Test
    fun `getAlbumInfo when api error returns failure`() {
        // given
        val artistName = "Michael Jackson"
        val albumName = "Thriller"
        val mbId = "123"

        coEvery {
            mockService.getAlbumInfoAsync(artistName, albumName, mbId)
        } returns mockk<ApiResult.Error<GetAlbumInfoResponse>>()

        // when
        val actual = runBlocking { sut.getAlbumInfo(artistName, albumName, mbId) }

        // then
        actual shouldBeEqualTo Result.Failure()
    }

    @Test
    fun `addAlbumToFavorites when db success returns success`() =
        runBlocking {
            // given
            val albumMbId = "mbId1"
            coEvery { mockAlbumDao.insertFavoriteAlbum(FavoriteAlbumRoomModel(albumMbId)) } returns Unit

            // when
            val actual = sut.addAlbumToFavorites(albumMbId)

            // then
            coVerify(exactly = 1) { mockAlbumDao.insertFavoriteAlbum(FavoriteAlbumRoomModel(albumMbId)) }
            actual shouldBeEqualTo Result.Success(Unit)
        }

    @Test
    fun `addAlbumToFavorites when db exception returns failure`() =
        runBlocking {
            // given
            val albumMbId = "mbId1"
            val exception = Exception("DB error")
            coEvery { mockAlbumDao.insertFavoriteAlbum(FavoriteAlbumRoomModel(albumMbId)) } throws exception

            // when
            val actual = sut.addAlbumToFavorites(albumMbId)

            // then
            actual shouldBeEqualTo Result.Failure(exception)
        }

    @Test
    fun `removeAlbumFromFavorites when db success returns success`() =
        runBlocking {
            // given
            val albumMbId = "mbId1"
            coEvery { mockAlbumDao.deleteFavoriteAlbum(FavoriteAlbumRoomModel(albumMbId)) } returns Unit

            // when
            val actual = sut.removeAlbumFromFavorites(albumMbId)

            // then
            coVerify(exactly = 1) { mockAlbumDao.deleteFavoriteAlbum(FavoriteAlbumRoomModel(albumMbId)) }
            actual shouldBeEqualTo Result.Success(Unit)
        }

    @Test
    fun `removeAlbumFromFavorites when db exception returns failure`() =
        runBlocking {
            // given
            val albumMbId = "mbId1"
            val exception = Exception("DB error")
            coEvery { mockAlbumDao.deleteFavoriteAlbum(FavoriteAlbumRoomModel(albumMbId)) } throws exception

            // when
            val actual = sut.removeAlbumFromFavorites(albumMbId)

            // then
            actual shouldBeEqualTo Result.Failure(exception)
        }

    @Test
    fun `isAlbumFavorite when album is favorited returns true`() =
        runBlocking {
            // given
            val albumMbId = "mbId1"
            coEvery { mockAlbumDao.isAlbumFavorite(albumMbId) } returns true

            // when
            val actual = sut.isAlbumFavorite(albumMbId)

            // then
            actual shouldBeEqualTo true
        }

    @Test
    fun `isAlbumFavorite when album is not favorited returns false`() =
        runBlocking {
            // given
            val albumMbId = "mbId1"
            coEvery { mockAlbumDao.isAlbumFavorite(albumMbId) } returns false

            // when
            val actual = sut.isAlbumFavorite(albumMbId)

            // then
            actual shouldBeEqualTo false
        }
}
