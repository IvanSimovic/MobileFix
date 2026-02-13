package com.simovic.meapp.birthday

import com.simovic.meapp.birthday.data.dataModule
import com.simovic.meapp.birthday.presentation.presentationModule

val featureBirthDayModules =
    listOf(
        presentationModule,
        dataModule,
    )
