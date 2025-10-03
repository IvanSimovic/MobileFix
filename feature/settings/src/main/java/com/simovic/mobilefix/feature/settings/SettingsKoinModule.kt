package com.simovic.mobilefix.feature.settings

import com.simovic.mobilefix.feature.settings.data.dataModule
import com.simovic.mobilefix.feature.settings.domain.domainModule
import com.simovic.mobilefix.feature.settings.presentation.presentationModule

val featureSettingsModules =
    listOf(
        presentationModule,
        domainModule,
        dataModule,
    )
