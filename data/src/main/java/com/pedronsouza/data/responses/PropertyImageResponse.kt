package com.pedronsouza.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class PropertyImageResponse(
    val prefix: String,
    val suffix: String
)