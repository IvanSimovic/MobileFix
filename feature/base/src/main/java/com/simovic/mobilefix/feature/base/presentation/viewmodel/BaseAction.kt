package com.simovic.mobilefix.feature.base.presentation.viewmodel

interface BaseAction<State> {
    fun reduce(state: State): State
}
