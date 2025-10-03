package com.simovic.mobilefix.feature.settings.presentation.screen.aboutlibraries

import androidx.compose.runtime.Immutable
import com.simovic.mobilefix.feature.base.presentation.viewmodel.BaseState

@Immutable
internal sealed interface AboutLibrariesUiState : BaseState {
    @Immutable
    data object Content : AboutLibrariesUiState
}
