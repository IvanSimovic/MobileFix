package com.simovic.mobilefix.feature.album.domain.model

import com.simovic.mobilefix.feature.album.domain.enum.ImageSize

internal data class Image(
    val url: String,
    val size: ImageSize,
)
