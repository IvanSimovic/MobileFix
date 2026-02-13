package com.simovic.meapp.birthday.data.repository

import com.simovic.meapp.birthday.data.datasource.database.BirthdayDao
import com.simovic.meapp.birthday.data.mapper.BirthdayMapper
import com.simovic.meapp.birthday.domain.model.BirthDay
import com.simovic.meapp.birthday.domain.model.CreateBirthDay
import com.simovic.meapp.birthday.domain.repository.BirthDayRepo
import com.simovic.meapp.feature.base.domain.result.Result

internal class BirthDayRepoImpl(
    private val birthdayDao: BirthdayDao,
    private val birthdayMapper: BirthdayMapper,
) : BirthDayRepo {
    override suspend fun get(): Result<List<BirthDay>> =
        try {
            val result = birthdayDao.getAll().map { birthdayMapper.roomToDomain(it) }
            Result.Success(result)
        } catch (e: Exception) {
            Result.Failure(e)
        }

    override suspend fun add(birthday: CreateBirthDay): Result<Unit> {
        try {
            birthdayDao.insert(birthdayMapper.create(birthday))
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Failure(e)
        }
    }

    override suspend fun remove(id: Long): Result<Unit> {
        try {
            birthdayDao.delete(id)
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Failure(e)
        }
    }
}
