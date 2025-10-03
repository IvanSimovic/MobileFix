package com.simovic.mobilefix.feature.album.data.datasource.api.model

import com.simovic.mobilefix.feature.album.data.datasource.database.model.ImageRoomModel
import com.simovic.mobilefix.feature.album.domain.model.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ImageApiModel(
    @SerialName("#text") val url: String,
    @SerialName("size") val size: ImageSizeApiModel,
)

internal fun ImageApiModel.toDomainModel() =
    Image(
        url = this.url,
        size = this.size.toDomainModel(),
    )

internal fun ImageApiModel.toRoomModel() = this.size.toRoomModel()?.let { ImageRoomModel(this.url, it) }
