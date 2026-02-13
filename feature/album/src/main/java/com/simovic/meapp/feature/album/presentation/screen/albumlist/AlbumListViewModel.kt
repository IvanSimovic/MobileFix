package com.simovic.meapp.feature.album.presentation.screen.albumlist

import androidx.lifecycle.viewModelScope
import com.simovic.meapp.feature.base.domain.result.Result
import com.simovic.meapp.feature.album.domain.usecase.GetAlbumListUseCase
import com.simovic.meapp.feature.base.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
internal class AlbumListViewModel(
    private val getAlbumListUseCase: GetAlbumListUseCase,
) : BaseViewModel<AlbumListUiState, AlbumListAction>(AlbumListUiState.Loading) {

    private val query = MutableStateFlow("")

    private val _uiState = MutableStateFlow<AlbumListUiState>(AlbumListUiState.Loading)
    val uiState: StateFlow<AlbumListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            query
                .debounce(500)
                .distinctUntilChanged()
                .filter { it.isBlank() || it.length >= 2 }
                .flatMapLatest { query ->
                    flow {
                        emit(AlbumListUiState.Loading)
                        when (val result = getAlbumListUseCase(query)) {
                            is Result.Success -> {
                                val albums = result.value
                                emit(
                                    if (albums.isEmpty()) AlbumListUiState.Error
                                    else AlbumListUiState.Content(albums = albums)
                                )
                            }
                            is Result.Failure -> {
                                emit(AlbumListUiState.Error)
                            }
                        }
                    }.catch {
                        emit(AlbumListUiState.Error)
                    }
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = AlbumListUiState.Loading
                )
                .collect { _uiState.value = it }
        }
    }

    fun onQueryChanged(newQuery: String) {
        query.value = newQuery.trim()
    }

    fun clearQuery() {
        query.value = ""
    }

}
