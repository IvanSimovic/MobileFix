package com.simovic.mobilefix.feature.album.presentation.screen.albumlist

import com.simovic.mobilefix.feature.album.domain.model.Album
import com.simovic.mobilefix.feature.base.presentation.viewmodel.BaseAction

internal sealed interface AlbumListAction : BaseAction<AlbumListUiState> {
    object AlbumListLoadStart : AlbumListAction {
        override fun reduce(state: AlbumListUiState) = AlbumListUiState.Loading
    }

    class AlbumListLoadSuccess(
        private val albums: List<Album>,
    ) : AlbumListAction {
        override fun reduce(state: AlbumListUiState) = AlbumListUiState.Content(albums)
    }

    object AlbumListLoadFailure : AlbumListAction {
        override fun reduce(state: AlbumListUiState) = AlbumListUiState.Error
    }
}
