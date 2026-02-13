package com.simovic.meapp.birthday.presentation.screen.birthdaylist

import androidx.lifecycle.viewModelScope
import com.simovic.meapp.birthday.domain.repository.BirthDayRepo
import com.simovic.meapp.feature.base.domain.result.Result
import com.simovic.meapp.feature.base.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class BirthDayViewModel(
    private val birthDayRepo: BirthDayRepo,
) : BaseViewModel<BirthDayUiState, BirthDayAction>(BirthDayUiState.Loading) {
    private var job: Job? = null

    fun get() {
        if (job != null) {
            job?.cancel()
            job = null
        }

        job =
            viewModelScope.launch {
                birthDayRepo.get().also { result ->
                    val liveFeedAction =
                        when (result) {
                            is Result.Success -> {
                                BirthDayAction.Success(result.value.toBirthDayListModel())
                            }

                            is Result.Failure -> {
                                BirthDayAction.Fail
                            }
                        }
                    sendAction(liveFeedAction)
                }
            }
    }

    fun selectForDelete(id: Long) {
        val state = uiStateFlow.value as? BirthDayUiState.Content ?: return
        val updatedList = state.birthdays.map { if (id == it.id) it.copy(isMarkedForDelete = !it.isMarkedForDelete) else it }
        val isMarkedForDelete = updatedList.firstOrNull { it.isMarkedForDelete } != null
        sendAction(BirthDayAction.UpdateMarkedForDelete(updatedList, isMarkedForDelete))
    }

    fun delete() {
        val state = uiStateFlow.value as? BirthDayUiState.Content ?: return
        if (!state.isDeleteModeActive) {
            sendAction(BirthDayAction.StartDelete(state.birthdays))
        } else if (!state.isSelectedForDelete) {
            sendAction(BirthDayAction.StopDelete(state.birthdays))
        } else {
            viewModelScope.launch {
                state.birthdays.filter { it.isMarkedForDelete }.forEach { item ->
                    birthDayRepo.remove(item.id)
                }
                sendAction(BirthDayAction.StopDelete(state.birthdays.filter { !it.isMarkedForDelete }))
            }
        }
    }
}
