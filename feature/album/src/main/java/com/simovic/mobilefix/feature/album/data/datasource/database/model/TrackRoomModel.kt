package com.simovic.mobilefix.feature.album.data.datasource.database.model

import com.simovic.mobilefix.feature.album.domain.model.Track
import kotlinx.serialization.Serializable

@Serializable
internal data class TrackRoomModel(
    val name: String,
    val duration: Int? = null,
)

internal fun TrackRoomModel.toDomainModel() =
    Track(
        name = this.name,
        duration = this.duration,
    )
