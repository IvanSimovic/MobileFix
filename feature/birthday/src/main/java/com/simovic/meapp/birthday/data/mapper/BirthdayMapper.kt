package com.simovic.meapp.birthday.data.mapper

import com.simovic.meapp.birthday.data.datasource.database.model.BirthDayRoomModel
import com.simovic.meapp.birthday.domain.model.BirthDay
import com.simovic.meapp.birthday.domain.model.CreateBirthDay

internal class BirthdayMapper {
    fun domainToRoom(domainModel: BirthDay) =
        BirthDayRoomModel(
            id = domainModel.id,
            name = domainModel.name,
            date = domainModel.date,
        )

    fun create(domainModel: CreateBirthDay) =
        BirthDayRoomModel(
            id = 0,
            name = domainModel.name,
            date = domainModel.date,
        )

    fun roomToDomain(room: BirthDayRoomModel): BirthDay =
        BirthDay(
            id = room.id,
            name = room.name,
            date = room.date,
        )
}
