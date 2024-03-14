package com.pedronsouza.data.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PropertyRatingBreakdownResponse(
    val security: Int,
    val location: Int,
    val staff: Int,
    val clean: Int,
    val facilities: Int,
    val average: Int,
    @SerialName("value") val overall: Int
)