package com.pedronsouza.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class PropertyRatingResponse(
    val overall: Int,
    val numberOfRatings: Int
)