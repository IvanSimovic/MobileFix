package com.simovic.meapp.birthday.domain.model

import java.time.LocalDate

data class BirthDay(
    val id: Long,
    val name: String,
    val date: LocalDate,
)

data class CreateBirthDay(
    val name: String,
    val date: LocalDate,
)
