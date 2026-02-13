package com.simovic.meapp.birthday.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.simovic.meapp.birthday.data.datasource.database.model.BirthDayRoomModel
import com.simovic.meapp.feature.base.util.DateConverter

@TypeConverters(DateConverter::class)
@Database(entities = [BirthDayRoomModel::class], version = 1, exportSchema = false)
internal abstract class BirthdayDatabase : RoomDatabase() {
    abstract fun birthDays(): BirthdayDao
}
