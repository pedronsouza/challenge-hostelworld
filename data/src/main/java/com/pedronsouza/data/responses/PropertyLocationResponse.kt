package com.pedronsouza.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class PropertyLocationResponse(
    val city: LocationCityResponse
)
