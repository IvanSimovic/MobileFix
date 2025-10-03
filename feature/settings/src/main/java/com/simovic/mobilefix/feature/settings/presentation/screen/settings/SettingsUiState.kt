package com.simovic.mobilefix.feature.settings.presentation.screen.settings

import androidx.compose.runtime.Immutable
import com.simovic.mobilefix.feature.base.presentation.viewmodel.BaseState

@Immutable
internal sealed interface SettingsUiState : BaseState {
    @Immutable
    data object Content : SettingsUiState
}
