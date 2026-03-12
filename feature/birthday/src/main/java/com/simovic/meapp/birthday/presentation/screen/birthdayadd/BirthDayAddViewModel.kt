package com.simovic.meapp.birthday.presentation.screen.birthdayadd

import androidx.lifecycle.viewModelScope
import com.simovic.meapp.birthday.domain.model.CreateBirthDay
import com.simovic.meapp.birthday.domain.repository.BirthDayRepo
import com.simovic.meapp.feature.base.presentation.viewmodel.BaseViewModel
import com.simovic.meapp.feature.base.util.DATE_PLACEHOLDER_YEAR
import kotlinx.coroutines.launch
import java.time.LocalDate

internal class BirthDayAddViewModel(
    private val birthDayRepo: BirthDayRepo,
) : BaseViewModel<BirthDayAddUiState, BirthDayAddAction>(BirthDayAddUiState.Empty) {
    fun add(
        name: String,
        dayOfMonth: Int,
        month: Int,
    ) {
        viewModelScope.launch {
            if (!name.isEmpty()) {
                birthDayRepo.add(CreateBirthDay(name, LocalDate.of(DATE_PLACEHOLDER_YEAR, month, dayOfMonth)))
                sendAction(BirthDayAddAction.Success())
            }
        }
    }
}
