package com.simovic.meapp.birthday.presentation.screen.birthdaylist

import com.simovic.meapp.birthday.domain.model.BirthDay
import com.simovic.meapp.feature.base.util.toBirthDay

data class BirthDayListModel(
    val id: Long,
    val name: String,
    val date: String,
    val isMarkedForDelete: Boolean,
)

fun List<BirthDay>.toBirthDayListModel() = this.map { it.toBirthDayListModel() }

fun BirthDay.toBirthDayListModel() =
    BirthDayListModel(
        id = id,
        name = name,
        date = date.toBirthDay(),
        isMarkedForDelete = false,
    )
