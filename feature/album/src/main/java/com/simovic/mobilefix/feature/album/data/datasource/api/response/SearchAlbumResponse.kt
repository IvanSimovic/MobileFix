package com.simovic.mobilefix.feature.album.data.datasource.api.response

import com.simovic.mobilefix.feature.album.data.datasource.api.model.SearchAlbumResultsApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SearchAlbumResponse(
    @SerialName("results") val results: SearchAlbumResultsApiModel,
)
