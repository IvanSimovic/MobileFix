package com.simovic.meapp.birthday.presentation.screen.birthdayadd

import com.simovic.meapp.feature.base.presentation.viewmodel.BaseAction

internal sealed interface BirthDayAddAction : BaseAction<BirthDayAddUiState> {
    class Success : BirthDayAddAction {
        override fun reduce(state: BirthDayAddUiState) = BirthDayAddUiState.Success
    }
}
