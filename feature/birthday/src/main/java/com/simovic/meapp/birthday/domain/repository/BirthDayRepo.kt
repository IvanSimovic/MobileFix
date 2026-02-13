package com.simovic.meapp.birthday.domain.repository

import com.simovic.meapp.birthday.domain.model.BirthDay
import com.simovic.meapp.birthday.domain.model.CreateBirthDay
import com.simovic.meapp.feature.base.domain.result.Result

internal interface BirthDayRepo {
    suspend fun get(): Result<List<BirthDay>>

    suspend fun add(birthday: CreateBirthDay): Result<Unit>

    suspend fun remove(id: Long): Result<Unit>
}
