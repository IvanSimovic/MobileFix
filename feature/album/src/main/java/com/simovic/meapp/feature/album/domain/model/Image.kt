package com.simovic.meapp.feature.album.domain.model

import com.simovic.meapp.feature.album.domain.enum.ImageSize

internal data class Image(
    val url: String,
    val size: ImageSize,
)
