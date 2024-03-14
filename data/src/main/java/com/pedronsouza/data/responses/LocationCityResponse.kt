package com.pedronsouza.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class LocationCityResponse(
    val id: String,
    val name: String,
    val country: String
)
