package com.simovic.meapp.feature.album.presentation.screen.albumlist

import androidx.lifecycle.viewModelScope
import com.simovic.meapp.feature.album.domain.usecase.GetAlbumListUseCase
import com.simovic.meapp.feature.base.domain.result.Result
import com.simovic.meapp.feature.base.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

private const val SEARCH_DEBOUNCE_MS = 500L

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
internal class AlbumListViewModel(
    private val getAlbumListUseCase: GetAlbumListUseCase,
) : BaseViewModel<AlbumListUiState, AlbumListAction>(AlbumListUiState.Loading) {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    init {
        viewModelScope.launch {
            _query
                .debounce(SEARCH_DEBOUNCE_MS)
                .distinctUntilChanged()
                .filter { it.isBlank() || it.length >= 2 }
                .flatMapLatest { query ->
                    flow {
                        emit(AlbumListAction.AlbumListLoadStart)
                        when (val result = getAlbumListUseCase(query)) {
                            is Result.Success -> {
                                val albums = result.value
                                emit(
                                    if (albums.isEmpty()) {
                                        AlbumListAction.AlbumListLoadFailure
                                    } else {
                                        AlbumListAction.AlbumListLoadSuccess(albums)
                                    },
                                )
                            }
                            is Result.Failure -> {
                                emit(AlbumListAction.AlbumListLoadFailure)
                            }
                        }
                    }.catch {
                        emit(AlbumListAction.AlbumListLoadFailure)
                    }
                }.collect { sendAction(it) }
        }
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery.trim()
    }
}
