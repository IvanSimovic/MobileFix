package com.simovic.meapp.feature.base.presentation.viewmodel

interface BaseAction<State> {
    fun reduce(state: State): State
}
