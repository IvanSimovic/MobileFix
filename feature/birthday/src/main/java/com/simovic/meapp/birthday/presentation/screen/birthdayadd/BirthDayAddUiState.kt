package com.simovic.meapp.birthday.presentation.screen.birthdayadd

import androidx.compose.runtime.Immutable
import com.simovic.meapp.feature.base.presentation.viewmodel.BaseState

@Immutable
internal sealed interface BirthDayAddUiState : BaseState {
    @Immutable
    data object Empty : BirthDayAddUiState

    @Immutable
    data object Success : BirthDayAddUiState
}
