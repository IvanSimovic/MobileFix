package com.simovic.meapp.birthday.presentation

import com.simovic.meapp.birthday.presentation.screen.birthdayadd.BirthDayAddViewModel
import com.simovic.meapp.birthday.presentation.screen.birthdaylist.BirthDayViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule =
    module {

        viewModelOf(::BirthDayViewModel)
        viewModelOf(::BirthDayAddViewModel)
    }
