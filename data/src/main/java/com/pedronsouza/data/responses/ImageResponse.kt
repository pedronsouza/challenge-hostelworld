package com.pedronsouza.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    val prefix: String,
    val suffix: String
)