package com.simovic.meapp.feature.album.data.datasource.database.model

import com.simovic.meapp.feature.album.domain.model.Image
import kotlinx.serialization.Serializable

@Serializable
internal data class ImageRoomModel(
    val url: String,
    val size: ImageSizeRoomModel,
)

internal fun ImageRoomModel.toDomainModel() = this.size.toDomainModel()?.let { Image(this.url, it) }
